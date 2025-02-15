package com.example.main.Payment.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
public class PaymentClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    private String userName;
    private String hotelId;
    private String paymentType;
    private String transactionNo;
    private LocalDateTime paymentDate;
    private String bookingNo;
    private long bookingId;
    private String paymentMethod;

    @Enumerated(EnumType.STRING) // Annotate to store enum as a string in the database
    private PaymentStatus paymentStatus;

    private String userEmail;
    private double totalAmount;
    private double totalPaid;
    private double pendingAmount;
    @Column(nullable = false, columnDefinition = "double precision default 0.0")
    private double recievedAmount;
//    @Column("")
@Column(nullable = false, columnDefinition = "double precision default 0.0")
    private double refundAmount;


    private double tax;

    private double cancelTax;

    private double cancelTaxCharge;

    private double refund;

    private double extraService;




}
