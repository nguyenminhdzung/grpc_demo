package com.dmn.assignment.task1.service.impl.seatmap;

import com.dmn.assignment.task1.model.CinemaRoom;
import com.dmn.assignment.task1.service.SeatInfo;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SeatMap {
    @Getter
    private String roomName;
    private int allowedDistance;
    private SeatStatus[][] statusMatrix;

    public SeatMap(CinemaRoom room) {
        this.roomName = room.getName();
        this.allowedDistance = room.getAllowedDistance();

        this.statusMatrix = new SeatStatus[room.getRows()][room.getSeatsPerRow()];
        for (int i = 0; i < room.getRows(); i++)
            for (int j = 0; j < room.getSeatsPerRow(); j++)
                this.statusMatrix[i][j] = SeatStatus.AVAILABLE;
    }

    public void markSeatReserved(List<SeatInfo> seats) {
        for (SeatInfo seat : seats)
            markSeatReserved(seat);
    }

    private void markSeatReserved(SeatInfo seat) {
        if (seat.getRowNumber() < 0 || seat.getRowNumber() >= statusMatrix.length
         || seat.getSeatNumber() < 0 || seat.getSeatNumber() >= statusMatrix[0].length)
            return;

        if (statusMatrix[seat.getRowNumber()][seat.getSeatNumber()] != SeatStatus.AVAILABLE)
            return;

        statusMatrix[seat.getRowNumber()][seat.getSeatNumber()] = SeatStatus.RESERVED;

        int left = seat.getSeatNumber() - (allowedDistance -1);
        int right = seat.getSeatNumber() + (allowedDistance -1);
        int top = seat.getRowNumber() - (allowedDistance -1);
        int bottom = seat.getRowNumber() + (allowedDistance -1);
        markSeatsProhibit(left, right, top, bottom);
    }

    private void markSeatsProhibit(int left, int right, int top, int bottom) {
        top = top < 0 ? 0 : top;
        bottom = bottom >= statusMatrix.length ? statusMatrix.length - 1 : bottom;
        left = left < 0 ? 0 : left;
        right = right >= statusMatrix[0].length ? statusMatrix[0].length - 1 : right;

        for(int rowNumber = top; rowNumber <= bottom; rowNumber++)
            for (int seatNumber = left; seatNumber <= right; seatNumber++)
                markSeatsProhibit(rowNumber, seatNumber);
    }

    private void markSeatsProhibit(int rowNumber, int seatNumber) {
        if (rowNumber < 0 || rowNumber >= statusMatrix.length
         || seatNumber < 0 || seatNumber >= statusMatrix[0].length)
            return;

        if (statusMatrix[rowNumber][seatNumber] != SeatStatus.AVAILABLE)
            return;

        statusMatrix[rowNumber][seatNumber] = SeatStatus.PROHIBIT;
    }

    public List<SeatInfo> getAvailableSeats() {
        List<SeatInfo> availableSeats = new ArrayList<>();

        for (int i = 0; i < statusMatrix.length; i++)
            for (int j = 0; j < statusMatrix[0].length; j++)
                if (statusMatrix[i][j] == SeatStatus.AVAILABLE)
                    availableSeats.add(SeatInfo.builder()
                                               .rowNumber(i).seatNumber(j)
                                               .build());

        return availableSeats;
    }

    public List<SeatInfo> fetchUnavailableSeats(List<SeatInfo> needReservingSeats) {
        if (needReservingSeats == null || needReservingSeats.size() <= 0)
            return needReservingSeats;

        return needReservingSeats.stream()
                                 .filter(s -> statusMatrix[s.getRowNumber()][s.getSeatNumber()] != SeatStatus.AVAILABLE)
                                 .collect(Collectors.toList());
    }
}
