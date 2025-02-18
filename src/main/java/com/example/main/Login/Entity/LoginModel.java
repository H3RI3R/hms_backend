package com.example.main.Login.Entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class LoginModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long loginId;

    private String userName;

    private String email;

    private String password;

    private String role;

    private String hotelId;

    private String userId;
    private String systemIP;
    private String location;
    private String browser;
    private LocalDateTime loginAt;
    private String lat;
    private String log;

    private Boolean isActive = true;
}
