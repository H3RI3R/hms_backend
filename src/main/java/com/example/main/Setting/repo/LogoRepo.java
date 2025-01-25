package com.example.main.Setting.repo;

import com.example.main.Setting.entity.Logo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogoRepo extends JpaRepository<Logo,Long> {

    Logo findByHotelId(String hotelId);
}
