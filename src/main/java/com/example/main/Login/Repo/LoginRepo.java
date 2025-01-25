package com.example.main.Login.Repo;

import com.example.main.Login.Entity.LoginModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LoginRepo extends JpaRepository<LoginModel,Long> {

    LoginModel findByEmail(String adminEmail);
    LoginModel findByHotelId(String hotelId);
    LoginModel getReferenceByEmail(String email);
}
