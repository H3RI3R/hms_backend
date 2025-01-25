package com.example.main.ManageHotel.Controller;

import com.example.main.ManageHotel.DTO.RoomTypesDTO;
import com.example.main.ManageHotel.Entity.RoomTypes;
import com.example.main.ManageHotel.Service.RoomTypesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RequestMapping("/roomTypes")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoomTypesController {

    private final RoomTypesService roomTypesService;


    @PostMapping("/add")
    ResponseEntity<?> createRoomTypes(
                                      @RequestHeader("Authorization") String token,
                                      @RequestParam("roomName") String roomName,
                                      @RequestParam("roomFare") Double roomFare,
                                      @RequestParam("adult") int adult,
                                      @RequestParam("children") int children,
                                      @RequestParam("cancelFee") double cancelFee,
                                      @RequestParam("facilities") String[] facilities,
                                      @RequestParam(value = "roomTypeImage",required = false) MultipartFile[] roomTypeImage,
                                      @RequestParam("roomTypeStatus") boolean roomTypeStatus,
                                      @RequestParam("featureStatus") boolean featureStatus,
                                      @RequestParam("roomDescription") String roomDescription,
                                      @RequestParam("cancelDescription") String cancelDescription,
                                      @RequestParam("totalBed") int totalBed,
                                      @RequestParam("amenitiesID") List<Long> amenitiesID,
                                      @RequestParam("bedTypesID") List<Long> bedTypesID) {
        return  roomTypesService.createRoomTypes(token, roomName,roomFare,adult,children,cancelFee,facilities,roomTypeImage,roomTypeStatus,featureStatus,roomDescription,cancelDescription,totalBed,amenitiesID,bedTypesID);

    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRoomTypes(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam(value = "roomName", required = false) String roomName,
            @RequestParam(value = "roomFare", required = false) Double roomFare,
            @RequestParam(value = "adult", required = false) Integer adult,
            @RequestParam(value = "children", required = false) Integer children,
            @RequestParam(value = "cancelFee", required = false) Double cancelFee,
            @RequestParam(value = "facilities", required = false) String[] facilities,
            @RequestParam(value = "roomTypeImage", required = false) MultipartFile[] roomTypeImage,
            @RequestParam(value = "roomTypeStatus", required = false) Boolean roomTypeStatus,
            @RequestParam(value = "featureStatus", required = false) Boolean featureStatus,
            @RequestParam(value = "roomDescription", required = false) String roomDescription,
            @RequestParam(value = "cancelDescription", required = false) String cancelDescription,
            @RequestParam(value = "totalBed", required = false) Integer totalBed,
            @RequestParam(value = "amenitiesID", required = false) List<Long> amenitiesID,
            @RequestParam(value = "bedTypesID", required = false) List<Long> bedTypesID) {

        return roomTypesService.updateRoomTypes(
                token, id, roomName, roomFare, adult, children, cancelFee, facilities,
                roomTypeImage, roomTypeStatus, featureStatus, roomDescription, cancelDescription,
                totalBed, amenitiesID, bedTypesID
        );
    }


//    @PutMapping("/update/{id}")
//    public ResponseEntity<?> updateRoomTypes(@RequestHeader("Authorization") String token,
//                                             @PathVariable Long id,
//                                             @RequestBody RoomTypes roomTypesDetails) {
//        return roomTypesService.updateRoomTypes(token, id, roomTypesDetails);
//    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getRoomTypesById(@RequestHeader("Authorization") String token,
                                              @PathVariable Long id) {
        return roomTypesService.getRoomTypesById(token, id);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllRoomTypes(@RequestHeader("Authorization") String token) {
        return roomTypesService.getAllRoomTypes(token);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRoomTypesById(@RequestHeader("Authorization") String token,
                                                 @PathVariable Long id) {
        return roomTypesService.deleteRoomTypesById(token, id);
    }

}
