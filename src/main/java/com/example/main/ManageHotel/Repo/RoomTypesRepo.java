package com.example.main.ManageHotel.Repo;

import com.example.main.ManageHotel.Entity.RoomTypes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypesRepo extends JpaRepository<RoomTypes,Long> {

    RoomTypes findByRoomTypesId(Long id);

    RoomTypes findByRoomNameContainingIgnoreCase(String roomName);
    List<RoomTypes> findByHotelId(String hotelId);

    RoomTypes findByRoomNameContainingIgnoreCaseAndHotelId(String roomName, String hotelId);

    List<RoomTypes> findByBedTypes_BedTypeId(long bedTypeId);
}
