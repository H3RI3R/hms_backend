package com.example.main.Report.Repo;

import com.example.main.Report.Entity.BookingAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingActionRepository extends JpaRepository<BookingAction, Long> {
    List<BookingAction> findByBookingNo(String bookingNo);
//    List<BookingAction> findByBookingNo(String bookingNo);

}