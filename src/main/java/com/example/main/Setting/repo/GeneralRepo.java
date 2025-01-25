package com.example.main.Setting.repo;

import com.example.main.Setting.entity.GeneralSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneralRepo extends JpaRepository<GeneralSetting,Long> {

    GeneralSetting findByHotelId(String hotelId);
}
