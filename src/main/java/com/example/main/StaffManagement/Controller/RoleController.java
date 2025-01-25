package com.example.main.StaffManagement.Controller;


import com.example.main.Configuration.ConfigClass;
import com.example.main.StaffManagement.DTO.CreateRoleDTO;
import com.example.main.StaffManagement.Service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/role")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoleController {
    private final RoleService roleService;
    private final ConfigClass configClass;

    @PostMapping("/create")
    public ResponseEntity<?> createRole(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateRoleDTO createRole){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return roleService.addNewRole(hotelId, createRole);
    }


    @PatchMapping("/update/{Id}")
    public ResponseEntity<?> updateRole(
            @RequestHeader("Authorization") String token,
            @PathVariable Long Id,
            @RequestParam CreateRoleDTO updateRole){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return roleService.updateRole(hotelId, Id, updateRole);
    }


    @GetMapping("/getOne/{Id}")
    public ResponseEntity<?> getRole(
            @RequestHeader("Authorization") String token,
            @PathVariable Long Id){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return roleService.getOneRole(hotelId, Id);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllRole(
            @RequestHeader("Authorization") String token){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return roleService.getAllRole(hotelId);
    }


    @DeleteMapping("/delete/{Id}")
    public ResponseEntity<?> deleteRole(
            @RequestHeader("Authorization") String token,
            @PathVariable Long Id){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return roleService.deleteRole(hotelId, Id);
    }

}
