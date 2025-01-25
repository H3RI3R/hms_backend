package com.example.main.ManageHotel.Repo;


import com.example.main.ManageHotel.Entity.Facility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FacilityRepo extends JpaRepository<Facility, Long> {

    Facility findByFacilityId(Long facilityId);

    List<Facility> findByHotelId(String hotelId);

    Facility findByFacilityNameContainingIgnoreCaseAndHotelId(String facilityName, String hotelId);
}

