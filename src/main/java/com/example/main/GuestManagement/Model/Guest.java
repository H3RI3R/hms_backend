package com.example.main.GuestManagement.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Entity
public class Guest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;

    private String lastName;

    private String hotelId;

    private String email;

    private String countryCode;

    private String phone;

    private Boolean isPhoneVerified = false;

    private Boolean isEmailVerified = false;

    private String address;

    private String city;

    private String state;

    private Integer zipCode;

    private String country;

    private String bookingId;

    private LocalDateTime checkInDate;

    private Boolean isUserBanned = false;

    private Boolean isDeleted = false;

    private Boolean isActive = false;

    private Boolean isSubscriber = false;

    private Instant createdAt = Instant.now();
}