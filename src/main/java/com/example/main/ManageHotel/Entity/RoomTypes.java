package com.example.main.ManageHotel.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class RoomTypes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long roomTypesId;

    private String roomName;
    private Double roomFare;
//    private int noOfRooms;
    private String hotelId;
    private String slug;

    private int adult;
    private int children;

    private double cancelFee;
    private String[] facilities;

    private String[] roomTypeImage;

    private boolean roomTypeStatus;
    private boolean featureStatus;

    private int totalBed;
    private String roomDescription;

    private String cancelDescription;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Amenities> amenities;


    @ManyToMany(fetch = FetchType.EAGER)
    private List<BedTypes> bedTypes;




}

