package com.example.main.Payment.Repo;

import com.example.main.Payment.entity.PaymentClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends JpaRepository<PaymentClass,Long> {

    PaymentClass findByBookingNo(String bookingNo);
}
