package com.example.main.Hotel.Repo;

import com.example.main.Hotel.Entity.Booking;
import com.example.main.Hotel.Entity.BookingPremium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepo extends JpaRepository<Booking,Long> {

    Booking findByGuestEmailAndHotelId(String email, String hotelId);

    Booking findByHotelIdAndBookingId(String hotelId, Long bookingId);

    List<Booking> findByCheckInDateBetweenAndHotelId(LocalDateTime startDateTime, LocalDateTime endDateTime, String hotelId);

    List<Booking> findByCheckOutDateBetweenAndHotelId(LocalDateTime startDateTime, LocalDateTime endDateTime, String hotelId);


    List<Booking> findByHotelIdAndBookingStatus(String hotelId, boolean b);

    List<Booking> findByHotelIdAndCheckOutDateBeforeAndBookingStatus(String hotelId, LocalDateTime currentDate, boolean b);

    List<Booking> findByHotelId(java.lang.String hotelId);

    //Booking findByHotelIdAndBookingNo(String hotelId, String bookingId);

    Booking findByBookingNo(String bookingNo);

    Booking findByRoomNoAndBookingStatus(int roomNo, boolean b);

    Booking findByBookingId(long bookingId);

    List<Booking> findByHotelIdAndBookingStatusAndCheckOutDateBefore(String hotelId, boolean b, LocalDateTime now);

    List<Booking> findByHotelIdAndBookingStatusAndCheckOutDateAfter(String hotelId, boolean b, LocalDateTime now);

    int countByRoomNoAndBookingStatus(int roomNo, boolean b);

    Booking findByBookingNoAndHotelId(String bookingId, String hotelId);

    List<Booking> findByHotelIdAndBookingCancel(String hotelId, boolean b);

    Booking findByBookingIdAndHotelId(long bookingId, String hotelId);

    List<Booking> findByHotelIdAndRefundable(String hotelId, boolean b);

    List<Booking> findByHotelIdAndBookingStatusAndCheckOutDate(String hotelId, boolean b, LocalDate today);

    List<Booking> findByHotelIdAndBookingStatusAndCheckInDateBefore(String hotelId, boolean b, LocalDateTime now);

    List<Booking> findByHotelIdAndBookingStatusAndCheckInDateAfter(String hotelId, boolean b, LocalDateTime now);

    Booking findByBookingIdAndBookingCancel(long bookingId, boolean b);

    List<Booking> findByHotelIdAndBookingStatusAndCheckOutDate(String hotelId, boolean b, LocalDateTime todayAtNoon);
}
