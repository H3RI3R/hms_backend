package com.example.main.Hotel.Repo;

import com.example.main.Hotel.Entity.BookingPremium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingPremiumRepo extends JpaRepository<BookingPremium,Long> {
    List<BookingPremium> findByHotelIdAndBookingNo(String hotelId, String bookingId);


    List<BookingPremium> findByHotelId(String hotelId);

    List<BookingPremium> findByBookingId(long bookingId);

    BookingPremium findByHotelIdAndBookingId(String hotelId, long bookingId);
}
