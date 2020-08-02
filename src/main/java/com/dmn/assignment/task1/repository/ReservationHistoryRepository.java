package com.dmn.assignment.task1.repository;

import com.dmn.assignment.task1.model.ReservationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationHistoryRepository extends JpaRepository<ReservationHistory, Long> {
}
