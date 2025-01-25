package com.example.main.Hotel.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class BookingPremium {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingPremiumId;
    private long bookingId;
    private String bookingNo;
    private String hotelId;
    private LocalDate  localDate;
    private int roomNo;
    private List<String> premiumServiceList;
    private List<Integer> quantity;


}
