package com.example.main.StaffManagement.Service;

import com.example.main.Exception.ResponseClass;
import com.example.main.StaffManagement.Model.Permission;
import com.example.main.StaffManagement.Repository.PermissionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PermissionService {

    private final PermissionRepo permissionRepo;

    public ResponseEntity<?> addNewPermission(String hotelId, String pagePermission) {
        Permission permission = new Permission();
        permission.setPermissionName(pagePermission);
        permission.setHotelId(hotelId);
        permissionRepo.save(permission);
        return ResponseClass.responseSuccess("new page permission created");
    }

    public ResponseEntity<?> updatePermission(String hotelId, Long id, String pagePermission) {
        Permission permission = permissionRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "record does not exist"));
        if(!permission.getHotelId().equals(hotelId)){ return ResponseClass.responseFailure("invalid Id"); }
        permission.setPermissionName(pagePermission);
        permissionRepo.save(permission);
        return ResponseClass.responseSuccess("page permission");
    }

    public ResponseEntity<?> getOnePermission(String hotelId, Long id) {
        Permission permission = permissionRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "record does not exist"));
        if(!permission.getHotelId().equals(hotelId)){ return ResponseClass.responseFailure("invalid Id"); }
        return ResponseClass.responseSuccess("page permission found","pagePermission",permission);
    }

    public ResponseEntity<?> getAllPermission(String hotelId) {
        List<Permission> permissions = permissionRepo.findAllByHotelId(hotelId);
        if(permissions.isEmpty()){ return ResponseClass.responseFailure("record does not exist"); }
        if(!permissions.get(0).getHotelId().equals(hotelId)){ return ResponseClass.responseFailure("invalid request"); }
        return ResponseClass.responseSuccess("page permission found", "pagePermission", permissions);
    }


    public ResponseEntity<?> deletePermission(String hotelId, Long id) {
        Permission permission = permissionRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "record does not exist"));
        if(!permission.getHotelId().equals(hotelId)){ return ResponseClass.responseFailure("invalid Id"); }
        permissionRepo.delete(permission);
        return ResponseClass.responseSuccess("page permission deleted");
    }
}
