package com.dmn.assignment.task1.service.impl;

import com.dmn.assignment.task1.exception.CinemaException;
import com.dmn.assignment.task1.model.CinemaRoom;
import com.dmn.assignment.task1.model.ReservedSeat;
import com.dmn.assignment.task1.repository.CinemaRoomRepository;
import com.dmn.assignment.task1.repository.ReservedSeatRepository;
import com.dmn.assignment.task1.service.CacheService;
import com.dmn.assignment.task1.service.CinemaService;
import com.dmn.assignment.task1.service.SeatInfo;
import com.dmn.assignment.task1.service.impl.seatmap.SeatMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CinemaServiceImpl extends BaseService implements CinemaService {

    @Autowired
    private CinemaRoomRepository cinemaRoomRepository;

    @Autowired
    private ReservedSeatRepository reservedSeatRepository;

    @Autowired
    private CacheService cacheService;

    public void configCinemaRoom(String name, int rows, int seatsPerRow, int minDistance) {
        int allowedDistance = minDistance < 0 ? 0 : minDistance;

        CinemaRoom cinemaRoom = cinemaRoomRepository.findFirstByName(name);
        if (cinemaRoom == null) {
            cinemaRoom = CinemaRoom.builder()
                                   .name(name)
                                   .rows(rows)
                                   .seatsPerRow(seatsPerRow)
                                   .allowedDistance(allowedDistance)
                                   .build();
        } else {
            lockForUpdate(cinemaRoom);

            cinemaRoom.setName(name);
            cinemaRoom.setRows(rows);
            cinemaRoom.setSeatsPerRow(seatsPerRow);
            cinemaRoom.setAllowedDistance(allowedDistance);
        }

        cinemaRoomRepository.save(cinemaRoom);
        reservedSeatRepository.deleteByRoom(cinemaRoom);

        SeatMap seatMap = rebuildSeatMap(cinemaRoom);
        cacheService.updateItem(cinemaRoom.getName(), seatMap);
    }

    public List<SeatInfo> getAvailableSeats(String roomName, int neededSeatCount) {
        CinemaRoom cinemaRoom = cinemaRoomRepository.findFirstByName(roomName);
        if (cinemaRoom == null)
            throw new CinemaException("Room doesn't exist.");

        SeatMap seatMap = getSeatMap(cinemaRoom);
        List<SeatInfo> availableSeats = seatMap.getAvailableSeats();

        if (availableSeats == null || availableSeats.size() < neededSeatCount)
            throw new CinemaException("There are less than " + neededSeatCount + " available seats");

        return availableSeats;
    }

    public void reserveSeats(String roomName, List<SeatInfo> needReservingSeats) {
        CinemaRoom cinemaRoom = cinemaRoomRepository.findFirstByName(roomName);
        if (cinemaRoom == null)
            throw new CinemaException("Room doesn't exist.");

        lockForUpdate(cinemaRoom);
        SeatMap seatMap = getSeatMap(cinemaRoom);

        List<SeatInfo> unavailableSeats = seatMap.fetchUnavailableSeats(needReservingSeats);
        if (unavailableSeats != null && unavailableSeats.size() > 0) {
            String unavailableSeatsText = unavailableSeats.stream()
                                                          .map(s -> String.format("[%d, %d]", s.getRowNumber(), s.getSeatNumber()))
                                                          .collect(Collectors.joining(", "));
            throw new CinemaException("Seats are not available for reserving: " + unavailableSeatsText);
        }

        for (SeatInfo seat : needReservingSeats) {
            reservedSeatRepository.save(ReservedSeat.builder()
                                                    .room(cinemaRoom)
                                                    .rowNumber(seat.getRowNumber())
                                                    .seatNumber(seat.getSeatNumber())
                                                    .reservedTime(new Date())
                                                    .build());
        }

        updateSeatMap(seatMap, needReservingSeats);
    }

    private void updateSeatMap(SeatMap seatMap, List<SeatInfo> reservedSeats) {
        seatMap.markSeatReserved(reservedSeats);
        cacheService.updateItem(seatMap.getRoomName(), seatMap);
    }

    private SeatMap rebuildSeatMap(CinemaRoom room) {
        SeatMap seatMap = new SeatMap(room);
        List<ReservedSeat> reservedSeats = reservedSeatRepository.findByRoom(room);
        if (reservedSeats != null && reservedSeats.size() > 0)
            seatMap.markSeatReserved(reservedSeats.stream()
                                                  .map(s -> SeatInfo.builder()
                                                                    .rowNumber(s.getRowNumber())
                                                                    .seatNumber(s.getSeatNumber())
                                                                    .build())
                                                  .collect(Collectors.toList()));

        return seatMap;
    }

    private SeatMap getSeatMap(CinemaRoom room) {
        SeatMap seatMap = cacheService.getItem(room.getName());

        if (seatMap == null) {
            seatMap = rebuildSeatMap(room);
            cacheService.updateItem(room.getName(), seatMap);
        }

        return seatMap;
    }
}
