package com.example.main.Login.Repo;

import com.example.main.Login.Entity.Otp;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpRepo extends JpaRepository<Otp, Integer> {

    Otp getReferenceByEmail(String email);


    void deleteByEmail(String email);

//    void deleteAllByEmail(String email);

    List<Otp> findAllByEmail(String email);
    Otp findByEmail(String email);


    @Modifying
    @Transactional
    @Query("DELETE FROM Otp e WHERE e.email = :email")
    void deleteAllByEmail(@Param("email") String email);
}