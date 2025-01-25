package com.example.main.ManageHotel.Repo;

import com.example.main.ManageHotel.Entity.BedTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BedTypeRepo extends JpaRepository<BedTypes,Long> {

    BedTypes findByBedTypeId(Long bedTypeId);

    List<BedTypes> findByHotelId(String hotelId);

    BedTypes findByBedNameContainingIgnoreCaseAndHotelId(String bedName, String hotelId);

    boolean existsByBedTypeIdAndHotelId(Long id, String hotelId);
}
