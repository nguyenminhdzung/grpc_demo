package com.dmn.assignment.task1.service;

import com.dmn.assignment.task1.endpoint.Seat;

import java.util.List;

public interface CinemaService {
    void configCinemaRoom(String name, int rows, int seatsPerRow, int minDistance);
    List<SeatInfo> getAvailableSeats(String roomName, int neededSeatCount);
    void reserveSeats(String roomName, List<Seat> needReservingSeats);
}
