package com.example.main.Subscribe.Model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class SubscriberMail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hotelId;

    private String subject;

    private String body;

    private List<Long> recipientIds;

    private List<String> recipientEmails;

    private int coolingPeriod;


}
