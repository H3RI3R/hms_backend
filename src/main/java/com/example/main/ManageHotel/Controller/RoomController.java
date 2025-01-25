package com.example.main.ManageHotel.Controller;

import com.example.main.ManageHotel.Entity.Room;
import com.example.main.ManageHotel.Service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/room")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoomController {

    private final RoomService roomService;


    // Add Room
    @PostMapping("/add")
    public ResponseEntity<?> addRoom(@RequestHeader("Authorization") String token, @RequestBody Room room) {
        return roomService.createRoom(token, room);
    }

    // Get Room by ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getRoomById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return roomService.getRoomById(token, id);
    }

    // Get All Rooms
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllRooms(@RequestHeader("Authorization") String token) {
        return roomService.getAllRooms(token);
    }

    // Update Room by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoomById(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestBody Room roomDetails) {
        return roomService.updateRoomById(token, id, roomDetails);
    }

    // Delete Room by ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRoomById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return roomService.deleteRoomById(token, id);
    }

}
