package com.example.main.ManageHotel.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
public class Amenities implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long amenitiesId;

    private String amenitiesName;
    private String hotelId;
    private String icon;

    private boolean status;


    @ManyToMany
    @JsonIgnore
    private List<RoomTypes> roomTypes;
}
