package com.example.main.Hotel.Controller;

import com.example.main.Hotel.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminController {


    private final AdminService adminService;


    @PostMapping("/addSuperAdmin")
    public ResponseEntity<?> registerSuperAdmin(
            @RequestHeader("SECRET")String secret,
            @RequestParam String adminName,
            @RequestParam String adminAddress,
            @RequestParam String adminEmail,
            @RequestParam String adminPhone,
            @RequestParam String adminPassword
    ){
        return adminService.createSuperAdmin(secret, adminName, adminAddress, adminEmail, adminPhone, adminPassword);
    }


    @GetMapping("/getAllAdmin")
    public ResponseEntity<?> getAllAdmin(@RequestHeader("Authorization") String token){
        return adminService.getAllAdmin(token);
    }

    @DeleteMapping("/deleteAdmin/{adminId}")
    public ResponseEntity<?> deleteAdmin(
            @PathVariable long adminId
    ){
        return adminService.deleteAdmin(adminId);
    }













}
