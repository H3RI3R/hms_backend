package com.example.main.Report.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class BookingPayments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String paymentType; // paymentReceived or paymentReturned
    private String bookingNo;
    private String userName;
    private String userEmail;
    private double totalAmount;
    private double totalPaid;
    private double pendingAmount;
    private String issuedBy;
    private LocalDateTime date;
}