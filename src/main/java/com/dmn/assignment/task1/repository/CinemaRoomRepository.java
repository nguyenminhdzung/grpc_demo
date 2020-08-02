package com.dmn.assignment.task1.repository;

import com.dmn.assignment.task1.model.CinemaRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CinemaRoomRepository extends JpaRepository<CinemaRoom, Integer> {
    CinemaRoom findFirstByName(String name);
}
