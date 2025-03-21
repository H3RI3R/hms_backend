package com.example.main.Payment.Repo;

import com.example.main.Payment.entity.PaymentClass;
import com.example.main.Payment.entity.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentClass,Long> {
    PaymentClass findByBookingNo(String bookingNo);

    Page<PaymentClass> findByHotelId(String hotelId, Pageable pageable);

    Page<PaymentClass> findByHotelIdAndPaymentStatus(String hotelId, PaymentStatus status, Pageable pageable);

    Page<PaymentClass> findByHotelIdAndUserEmailContaining(String hotelId, String userEmail, Pageable pageable);

    Page<PaymentClass> findByHotelIdAndUserNameContaining(String hotelId, String userName, Pageable pageable);

    Page<PaymentClass> findByHotelIdAndPaymentDateBetween(String hotelId, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

    Page<PaymentClass> findByHotelIdAndPaymentStatusAndPaymentDateBetween(String hotelId, PaymentStatus status, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);

    List<PaymentClass> findByTransactionNo(String transactionNo);

    Page<PaymentClass> findByHotelIdAndBookingId(String hotelId, long bookingId, Pageable pageable);
}
