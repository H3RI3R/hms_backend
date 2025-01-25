package com.example.main.StaffManagement.Controller;


import com.example.main.Configuration.ConfigClass;
import com.example.main.StaffManagement.Service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PermissionController {

    private final PermissionService permissionService;
    private final ConfigClass configClass;

    @PostMapping("/create")
    public ResponseEntity<?> createPermission(
            @RequestHeader("Authorization") String token,
            @RequestParam String pagePermission){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return permissionService.addNewPermission(hotelId, pagePermission);
    }


    @PatchMapping("/update/{Id}")
    public ResponseEntity<?> updatePermission(
            @RequestHeader("Authorization") String token,
            @PathVariable Long Id,
            @RequestParam String pagePermission){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return permissionService.updatePermission(hotelId, Id ,pagePermission);
    }


    @GetMapping("/getOne/{Id}")
    public ResponseEntity<?> getPermission(
            @RequestHeader("Authorization") String token,
            @PathVariable Long Id){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return permissionService.getOnePermission(hotelId, Id);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPermission(
            @RequestHeader("Authorization") String token){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return permissionService.getAllPermission(hotelId);
    }


    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deletePermission(
            @RequestHeader("Authorization") String token,
            @PathVariable Long Id){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return permissionService.deletePermission(hotelId, Id);
    }



}
