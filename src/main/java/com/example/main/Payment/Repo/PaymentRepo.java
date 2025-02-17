package com.example.main.Payment.Repo;

import com.example.main.Payment.entity.PaymentClass;
import com.example.main.Payment.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentClass,Long> {

    PaymentClass findByBookingNo(String bookingNo);
    List<PaymentClass> findByHotelId(String hotelId);
    List<PaymentClass> findByTransactionNo(String transactionNo);

    List<PaymentClass> findByHotelIdAndPaymentStatus(String hotelId, PaymentStatus status);
}
