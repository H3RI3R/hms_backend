package com.example.main.ManageHotel.Repo;

import com.example.main.ManageHotel.Entity.PremiumService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PremiumServiceRepo extends JpaRepository<PremiumService,Long> {
    PremiumService findByPreSerNameContainingIgnoreCaseAndHotelId(String preSerName, String hotelId);

    PremiumService findByPreServiceId(Long id);

    List<PremiumService> findByHotelId(String hotelId);
}
