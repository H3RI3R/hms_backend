package com.example.main.Setting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.sql.Time;
import java.time.LocalTime;

@Entity
@Data
public class GeneralSetting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long generalSettingId;

    private String siteTitle;
    private String currency;
    private String timeZone;
    private String siteBaseColor;
    private int siteRecordPage;
    private String currencyFormat;
    private String taxName;
    private float taxPercent;
    private LocalTime checkOutTime;
    private LocalTime checkInTime;
    private int upComingCheckInList;
    private int upComingCheckOutList;
    private String hotelId;



}
