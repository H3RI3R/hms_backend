package com.example.main.StaffManagement.Controller;


import com.example.main.Configuration.ConfigClass;
import com.example.main.StaffManagement.DTO.StaffDTO;
import com.example.main.StaffManagement.Service.StaffService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/staff")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StaffController {
    private final StaffService staffService;
    private final ConfigClass configClass;

    @PostMapping("/create")
    public ResponseEntity<?> createRole(
            @RequestHeader("Authorization") String token,
            @RequestBody StaffDTO staff) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return staffService.addNewStaff(hotelId, staff);
    }


    @PatchMapping("/update/{Id}")
    public ResponseEntity<?> updateRole(
            @RequestHeader("Authorization") String token,
            @PathVariable Long Id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Long roleId,
            @RequestParam(required = false) String password){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return staffService.updateStaff(hotelId, Id, name, username, email, address,roleId, password);
    }


    @GetMapping("/getOne/{Id}")
    public ResponseEntity<?> getRole(
            @RequestHeader("Authorization") String token,
            @PathVariable Long Id){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return staffService.getOneStaff(hotelId, Id);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllRole(
            @RequestHeader("Authorization") String token){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return staffService.getAllStaff(hotelId);
    }

    @GetMapping("/getBannedGuest")
    public ResponseEntity<?> getBannedGuest(
            @RequestHeader("Authorization") String token){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return staffService.getBannedGuest(hotelId);
    }


    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteRole(
            @RequestHeader("Authorization") String token,
            @PathVariable Long Id){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return staffService.deleteStaff(hotelId, Id);
    }

    @PutMapping("/staffBanStatus/{Id}")
    public ResponseEntity<?> banStaff(
            @RequestHeader("Authorization") String token,
            @PathVariable Long Id){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return staffService.banStaff(hotelId, Id);
    }

    @GetMapping("/loginStaff/{staffId}")
    public HashMap<String, String> loginStaff(
            @RequestHeader("Authorization") String token,
            @PathVariable Long staffId, HttpServletRequest request){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return staffService.loginStaff(hotelId, staffId,request);
    }


}
