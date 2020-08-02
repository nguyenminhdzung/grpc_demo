package com.dmn.assignment.task1.model;

import com.dmn.assignment.task1.model.keys.SeatId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
//@IdClass(SeatId.class)
public class ReservedSeat implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "room_id", referencedColumnName = "id")
    private CinemaRoom room;

    @Column
    private int rowNumber;

    @Column
    private int seatNumber;

    @Column(nullable = false)
    private Date reservedTime;
}
