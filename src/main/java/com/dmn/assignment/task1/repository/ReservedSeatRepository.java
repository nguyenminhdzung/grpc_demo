package com.dmn.assignment.task1.repository;

import com.dmn.assignment.task1.model.CinemaRoom;
import com.dmn.assignment.task1.model.ReservedSeat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservedSeatRepository extends JpaRepository<ReservedSeat, Integer> {
    void deleteByRoom(CinemaRoom room);
    List<ReservedSeat> findByRoom(CinemaRoom room);
}
