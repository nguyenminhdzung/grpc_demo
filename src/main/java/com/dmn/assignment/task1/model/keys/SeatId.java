//package com.dmn.assignment.task1.model.keys;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.io.Serializable;
//import java.util.Objects;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//public class SeatId implements Serializable {
//    private int roomId;
//    private int rowNumber;
//    private int seatNumber;
//
//    @Override
//    public boolean equals(Object obj) {
//        if (this == obj) return true;
//
//        if (obj == null || getClass() != obj.getClass()) return false;
//
//        SeatId seatId = (SeatId) obj;
//        return roomId == seatId.roomId
//            && rowNumber == seatId.rowNumber
//            && seatNumber == seatId.seatNumber;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(roomId, rowNumber, seatNumber);
//    }
//}