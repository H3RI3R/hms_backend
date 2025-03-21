package com.example.main.Hotel.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;
    private String hotelId;
    private String bookingNo;
    private String bookingType;
    private String guestName;
    private String guestEmail;
    private LocalDateTime checkInDate;
    private LocalDateTime checkOutDate;
    private double totalAmount;
    private double totalPaid;
    private  double pendingAmount;
    private String phoneNo;
    private String address;
    private int totalRoom;
    private int adults;
    private int children;
    private String paymentTime;
    private boolean bookingStatus=false;
    private boolean bookingCancel=false;
    private boolean refundable= false;
    private Double roomFare;

    @ElementCollection
    private List<Integer> roomNo;
}