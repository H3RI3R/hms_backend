package com.example.main.Setting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Logo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long logoId;

    private String logoWhite;
    private String logoBlack;
    private String favicon;
    private String hotelId;



}
