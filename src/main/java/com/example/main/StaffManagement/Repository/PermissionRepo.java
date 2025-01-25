package com.example.main.StaffManagement.Repository;

import com.example.main.StaffManagement.Model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, Long> {
    List<Permission> findAllByHotelId(String hotelId);

    @Query("SELECT p FROM Permission p WHERE p.id IN :idList")
    Set<Permission> findAllById(List<Long> idList);

}
