package com.example.main.ManageHotel.DTO;

import lombok.Data;

import java.util.List;

@Data
public class RoomTypesDTO {

    private String roomName;
    private Double roomFare;
    private int adult;
    private int children;
    private double cancelFee;
    private String[] facilities;
    private String[] roomTypeImage;
    private boolean roomTypeStatus;
    private boolean featureStatus;
    private String roomDescription;
    private String cancelDescription;
    private int totalBed;
    private List<Long> amenitiesID;
    private List<Long> bedTypesID;
}
