package com.example.main.GuestManagement.Repository;

import com.example.main.GuestManagement.Model.Guest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GuestRepo extends JpaRepository<Guest, Long> {

    @Query(value = "select * from guest where id =:id and is_deleted = false and hotel_id =:hotelId", nativeQuery = true)
    Guest findByIdAndHotelId(Long id, String hotelId);

    @Query(value =  "select * from guest where hotel_id =:hotelId and is_deleted = false",nativeQuery = true)
    List<Guest> findAllByHotelId(String hotelId);

    @Query(value = "select * from guest where hotel_id =:hotelId and is_active = true and is_deleted = false", nativeQuery = true)
    List<Guest> findAllByActiveHotelId(String hotelId);

    List<Guest> findAllByHotelIdAndIsUserBanned(String hotelId, boolean b);

    List<Guest> findAllByHotelIdAndIsEmailVerified(String hotelId, boolean b);

    List<Guest> findAllByHotelIdAndIsPhoneVerified(String hotelId, boolean b);

    Set<Guest> findByEmailAndHotelId(String email, String hotelId);

    Guest findByBookingId(String bookingId);

    List<Guest> findByHotelIdAndIsSubscriber(String hotelId, boolean b);
}
