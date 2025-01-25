package com.example.main.StaffManagement.Repository;

import com.example.main.StaffManagement.Model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StaffRepo extends JpaRepository<Staff, Long> {


    Staff findByIdAndHotelId(Long id,String hotelId);

    @Query(value =  "select * from staff where hotel_id =:hotelId and is_deleted = false",nativeQuery = true)
    List<Staff> findAllByHotelId(String hotelId);

    Staff findByEmail(String email);

    List<Staff> findAllByHotelIdAndStatus(String hotelId, String disabled);
}
