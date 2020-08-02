package com.dmn.assignment.task1.service;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatInfo {
    private int rowNumber;
    private int seatNumber;
}
