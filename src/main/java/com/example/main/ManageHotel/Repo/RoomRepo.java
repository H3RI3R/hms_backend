package com.example.main.ManageHotel.Repo;

import com.example.main.ManageHotel.Entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepo extends JpaRepository<Room,Long>
{

    Room findByRoomId(Long id);

    List<Room> findByHotelId(String hotelId);

    Room findByRoomNoAndHotelId(int roomNo, String hotelId);

//    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType " +
//            "AND r.hotelId = :hotelId " +
//            "AND (r.checkInDate IS NULL OR :currentDate >= r.checkOutDate OR :currentDate < r.checkInDate) " +
//            "AND r.status = true " +
//            "AND r.availableStatus = true")
//    List<Room> findAvailableRoom(@Param("roomType") String roomType,
//                                 @Param("currentDate") LocalDateTime currentDate,
//                                 @Param("hotelId") String hotelId);


//    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType " +
//            "AND r.hotelId = :hotelId " +
//            "AND (r.checkInDate IS NULL OR :currentDate >= r.checkOutDate OR :currentDate < r.checkInDate) " +
//            "AND r.status = true " +
//            "AND r.availableStatus = true")
//    List<Room> findAvailableRoom(@Param("roomType") String roomType,
//                                 @Param("currentDate") LocalDateTime currentDate,
//                                 @Param("hotelId") String hotelId);


    @Query("SELECT r FROM Room r WHERE r.roomType = :roomType " +
            "AND r.hotelId = :hotelId " +
            "AND r.status = true " +
            "AND r.availableStatus = true")
    List<Room> findAvailableRoom(@Param("roomType") String roomType,
                                 @Param("hotelId") String hotelId);


    Room findByHotelIdAndRoomNo(String hotelId, Integer roomNumber);

    Room findByHotelIdAndRoomNoAndStatus(String hotelId, Integer roomNumber, boolean b);


//    List<Room> findByRoomTypeAndHotelIdAndAvailableStatusTrue(String roomType, String hotelId);
    List<Room> findByRoomTypeAndHotelIdAndAvailableStatusTrue(String roomType, String hotelId);

    List<Room> findByHotelIdAndAvailableStatus(String hotelId, boolean b);

    List<Room> findByHotelIdAndStatus(String hotelId, boolean b);

    List<Room> findByRoomTypeAndHotelIdAndAvailableStatusTrueAndStatusTrue(String roomName, String hotelId);
}
