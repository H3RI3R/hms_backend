package com.example.main.ManageHotel.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class PremiumService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preServiceId;
    private String preSerName;
    private String hotelId;
    private boolean status;
    private double price;
}
