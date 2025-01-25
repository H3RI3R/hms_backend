package com.example.main.ManageHotel.Service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.ManageHotel.Entity.Room;
import com.example.main.ManageHotel.Entity.RoomTypes;
import com.example.main.ManageHotel.Repo.RoomRepo;
import com.example.main.ManageHotel.Repo.RoomTypesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoomService {

    private final ConfigClass config;
    private final RoomRepo roomRepository;
    private final RoomTypesRepo roomTypesRepo;


    public ResponseEntity<?> createRoom(String token, Room room) {
        String hotelId = config.tokenValue(token, "hotelId");

        room.setHotelId(hotelId);
        if (room.getRoomNo() != 0) {
            Room existingRoom = roomRepository.findByRoomNoAndHotelId(room.getRoomNo(), hotelId);
            if (existingRoom != null) {
                return ResponseClass.responseFailure("Room number already exists in this hotel");
            }
        }
        RoomTypes existingRoomType = roomTypesRepo.findByRoomNameContainingIgnoreCaseAndHotelId(room.getRoomType(), hotelId);
        if (existingRoomType == null) {
            return ResponseClass.responseFailure("Room type does not exist in this hotel");
        }
        room.setStatus(true);
        roomRepository.save(room);
        return ResponseClass.responseSuccess("Room created successfully");
    }

    // Get Room by ID
    public ResponseEntity<?> getRoomById(String token, Long id) {
        String hotelId = config.tokenValue(token, "hotelId");
        Room room = roomRepository.findByRoomId(id);
        if (room == null && !(room.getHotelId().equals(hotelId))) {
            return ResponseClass.responseFailure("Room not found in this hotel");
        }
        return ResponseClass.responseSuccess("Room get By Id","room",room);
    }

    // Get All Rooms
    public ResponseEntity<?> getAllRooms(String token) {
        String hotelId = config.tokenValue(token,"hotelId");
        List<Room> rooms = roomRepository.findByHotelId(hotelId);
        return ResponseClass.responseSuccess("Get All Room","room",rooms);
    }

    // Update Room by ID
    public ResponseEntity<?> updateRoomById(String token, Long id, Room roomDetails) {
        String hotelId = config.tokenValue(token,"hotelId");
        Room room = roomRepository.findByRoomId(id);
        if (room == null && !(room.getHotelId().equals(hotelId))) {
            return ResponseClass.responseFailure("Room not found in this hotel");
        }

        if (roomDetails.getRoomNo() != 0) {
            Room existingRoom = roomRepository.findByRoomNoAndHotelId(roomDetails.getRoomNo(), hotelId);
            if (existingRoom != null && existingRoom.getRoomId() != id) {
                return ResponseClass.responseFailure("Room number already exists in this hotel");
            }
            room.setRoomNo(roomDetails.getRoomNo());
        }
        if (roomDetails.getRoomType() != null) {
            room.setRoomType(roomDetails.getRoomType());
        }
        if(roomDetails.getStatus() != null) {
            room.setStatus(roomDetails.getStatus());
        }


        roomRepository.save(room);
        return ResponseClass.responseSuccess("Room update successfully");
    }

    public ResponseEntity<?> deleteRoomById(String token, Long id) {

        String hotelId = config.tokenValue(token, "hotelId");
        Room room = roomRepository.findByRoomId(id);
        if (room == null && !(room.getHotelId().equals(hotelId))) {
            return ResponseClass.responseFailure("Room not found in this hotel");
        }
        roomRepository.deleteById(id);
        return ResponseClass.responseSuccess("Room delete successfully");
    }
}
