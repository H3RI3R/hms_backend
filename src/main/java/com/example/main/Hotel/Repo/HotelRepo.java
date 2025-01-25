package com.example.main.Hotel.Repo;

import com.example.main.Hotel.Entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepo extends JpaRepository<Hotel,Long> {
    Hotel findByHotelName(String hotelName);
    Hotel findByHotelId(long hotelId);

    List<Hotel> findByStatus(boolean b);

    Hotel findByStHotelId(String hotelId);
}
