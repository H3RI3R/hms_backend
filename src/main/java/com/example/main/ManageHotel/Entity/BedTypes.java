package com.example.main.ManageHotel.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class BedTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bedTypeId;
    private String bedName;
    private String hotelId;
    private String bedImage;
    private boolean status;


    @ManyToMany
    @JsonIgnore
    private List<RoomTypes> roomTypes;
}
