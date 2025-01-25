package com.example.main.Hotel.Repo;

import com.example.main.Hotel.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepo extends JpaRepository<Admin,Long> {
    Admin findByAdminEmail(String adminEmail);

    Admin findByAdminId(long adminId);
}
