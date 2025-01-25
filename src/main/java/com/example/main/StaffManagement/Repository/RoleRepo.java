package com.example.main.StaffManagement.Repository;

import com.example.main.StaffManagement.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {

    List<Role> findAllByHotelId(String hotelId);
}
