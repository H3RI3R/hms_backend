package com.example.main.ManageHotel.Repo;

import com.example.main.ManageHotel.Entity.Amenities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AmenitiesRepo  extends JpaRepository<Amenities,Long> {
        Amenities findByAmenitiesId(long amenitiesId);

    List<Amenities> findByHotelId(String hotelId);

    Amenities findByAmenitiesNameContainingIgnoreCaseAndHotelId(String amenitiesName, String hotelId);

    boolean existsByAmenitiesIdAndHotelId(Long id, String hotelId);
}
