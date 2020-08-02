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
    }

    public List<SeatInfo> getAvailableSeats(int neededSeatCount) {
        return Lists.newArrayList(SeatInfo.builder()
                                          .rowNumber(1).seatNumber(2)
                                          .build(),
                                  SeatInfo.builder()
                                          .rowNumber(3).seatNumber(4)
                                          .build());
    }

    public void reserveSeats(List<Seat> needReservingSeats) {
        // Tim cac seat ngoi cung nhau -> tim ra so ghe canh nhau max
        // goi den availableSeats -> neu ds seats can reserve co thuoc available ko?
    }
}
