package com.example.main.Hotel.Entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class Hotel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long hotelId;
    private String hotelName;
    private String subTitle;
    private String destination;
    private boolean status;
    private LocalDate hotelDate;
    private String hotelClass;
    private String phoneNo;
    private String address;
    private String hotelEmail;
    private String description;
    private String stHotelId;
    private String[] hotelImageUrl;
}
