package com.dmn.assignment.task1.service.impl;

import com.dmn.assignment.task1.endpoint.Seat;
import com.dmn.assignment.task1.model.CinemaRoom;
import com.dmn.assignment.task1.repository.CinemaRoomRepository;
import com.dmn.assignment.task1.repository.ReservedSeatRepository;
import com.dmn.assignment.task1.service.CinemaService;
import com.dmn.assignment.task1.service.SeatInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CinemaServiceImpl extends BaseService implements CinemaService {

    @Autowired
    private CinemaRoomRepository cinemaRoomRepository;

    @Autowired
    private ReservedSeatRepository reservedSeatRepository;

    public void configCinemaRoom(String name, int rows, int seatsPerRow, int minDistance, boolean isResetReservation) {
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
        if (isResetReservation)
            reservedSeatRepository.deleteByRoom(cinemaRoom);

        rebuildSeatMap();
    }

    public List<SeatInfo> getAvailableSeats(int neededSeatCount) {
        // get available-marked seats from map
        return Lists.newArrayList(SeatInfo.builder()
                                          .rowNumber(1).seatNumber(2)
                                          .build(),
                                  SeatInfo.builder()
                                          .rowNumber(3).seatNumber(4)
                                          .build());
    }

    public void reserveSeats(List<Seat> needReservingSeats) {
        // lock the cinema room
        // check needReservingSeats exist in seats from getAvailableSeats
        // reserve seats
        updateSeatMap();
    }

    private void updateSeatMap() {
        // update seat matrix with new reserved seats
    }

    private void rebuildSeatMap() {
        // build a seat matrix of room that contains seats in
        // - reserved: from reserved seats
        // - prohibited: has mahattan distance < allowedDistance (calculated from reserved
        // - available: the rest
    }
}
