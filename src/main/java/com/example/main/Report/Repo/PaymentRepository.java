package com.example.main.Report.Repo;

import com.example.main.Report.Entity.BookingPayments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<BookingPayments, Long> {
    List<BookingPayments> findByPaymentType(String paymentType);
    List<BookingPayments> findByPaymentTypeAndUserEmail(String paymentType,String userEmail);
}