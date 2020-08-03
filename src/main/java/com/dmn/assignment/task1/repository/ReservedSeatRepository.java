package com.dmn.assignment.task1.repository;

import com.dmn.assignment.task1.model.CinemaRoom;
import com.dmn.assignment.task1.model.ReservedSeat;
import com.dmn.assignment.task1.model.keys.SeatId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, SeatId> {
    void deleteByRoom(CinemaRoom room);
    List<ReservedSeat> findByRoom(CinemaRoom room);
}
