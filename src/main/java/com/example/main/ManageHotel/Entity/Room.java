package com.example.main.ManageHotel.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roomId;
    private int roomNo;
    private String roomType;
    private Boolean status;
    private String hotelId;
    private Boolean availableStatus=true;

}
