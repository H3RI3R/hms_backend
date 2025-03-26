package com.example.main.Report.Repo;

import com.example.main.Report.Entity.BookingPayments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<BookingPayments, Long> {
    List<BookingPayments> findByPaymentType(String paymentType);
    Page<BookingPayments> findByPaymentType(String paymentType,Pageable pageable);
    List<BookingPayments> findByPaymentTypeAndUserEmail(String paymentType,String userEmail);
    Page<BookingPayments> findByPaymentTypeAndUserEmail(String paymentType, String userEmail , Pageable pageable);

    List<BookingPayments> findByPaymentTypeAndDateBetween(String paymentReceived, LocalDateTime startDate, LocalDateTime endDate);
    Page<BookingPayments> findByPaymentTypeAndDateBetween(String paymentReceived, LocalDateTime startDate, LocalDateTime endDate , Pageable pageable);
    List<BookingPayments> findByPaymentTypeAndBookingNo(String paymentReceived, String searchkey);
    Page<BookingPayments> findByPaymentTypeAndBookingNo(String paymentReceived, String searchkey ,Pageable pageable);
}