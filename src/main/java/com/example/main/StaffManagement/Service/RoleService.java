package com.example.main.StaffManagement.Service;

import com.example.main.Exception.ResponseClass;
import com.example.main.StaffManagement.DTO.CreateRoleDTO;
import com.example.main.StaffManagement.Model.Permission;
import com.example.main.StaffManagement.Model.Role;
import com.example.main.StaffManagement.Repository.PermissionRepo;
import com.example.main.StaffManagement.Repository.RoleRepo;
import com.example.main.Support.DateTimeWeekDay;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.*;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoleService {
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;

    public ResponseEntity<?> addNewRole(String hotelId, CreateRoleDTO createRole) {
        Role role = new Role();
        role.setRoleName(createRole.getRoleName());
        role.setHotelId(hotelId);
        List<Permission> permissions = new ArrayList<>();
        for(String pagePermission : createRole.getPagePermissions()){
            Permission thisPermission = new Permission();
            thisPermission.setHotelId(hotelId);
            thisPermission.setPermissionName(pagePermission);
            permissions.add(thisPermission);
        }
        permissionRepo.saveAll(permissions);
        role.setPermissions(Set.copyOf(permissions));
        roleRepo.save(role);
        return ResponseClass.responseSuccess("role created");
    }

    public ResponseEntity<?> updateRole(String hotelId, Long id, CreateRoleDTO updateRole) {
        Role role = roleRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "role not found"));
        if(role.getHotelId().equals(hotelId)){
            role.setRoleName(updateRole.getRoleName());
            List<Permission> permissions = new ArrayList<>();
            if(updateRole.getPagePermissions() != null){
                for(String pagePermission : updateRole.getPagePermissions()){
                    Permission thisPermission = new Permission();
                    thisPermission.setHotelId(hotelId);
                    thisPermission.setPermissionName(pagePermission);
                    permissionRepo.save(thisPermission);
                    permissions.add(thisPermission);
                }
            }
            if(updateRole.getRemovePermissions() != null){
                for(String pagePermission : updateRole.getRemovePermissions()){
                    for (Permission mappedPermission : role.getPermissions()) {
                        if (mappedPermission.getPermissionName().equals(pagePermission)) {
                            mappedPermission.getRoles().remove(role);
                            permissionRepo.save(mappedPermission);
                            role.getPermissions().remove(mappedPermission);
                            roleRepo.save(role);
                            permissionRepo.delete(mappedPermission);
                        }
                    }
                }
            }
            role.setPermissions(Set.copyOf(permissions));
            roleRepo.save(role);
            return ResponseClass.responseSuccess("role updated");
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "invalid request");
    }

    public ResponseEntity<?> getOneRole(String hotelId, Long id) {
        Role role = roleRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "role not found"));
        if(!role.getHotelId().equals(hotelId)){ return ResponseClass.responseFailure("invalid request"); }
        RoleResponse roleResponse = new RoleResponse(role.getId(), role.getRoleName(), new DateTimeWeekDay((role.getCreatedAt() ==null ? Instant.now() : role.getCreatedAt())), role.getPermissions());
        return ResponseClass.responseSuccess("role found", "role", roleResponse);
    }

    public ResponseEntity<?> getAllRole(String hotelId) {
        List<Role> roles = roleRepo.findAllByHotelId(hotelId);
        if(roles.isEmpty()){ return ResponseClass.responseFailure("roles not found"); }
        if(!roles.get(0).getHotelId().equals(hotelId)){ return ResponseClass.responseFailure("invalid request");}

        List<RoleResponse> roleResponses = roles.stream().map(role -> new RoleResponse(
                role.getId(),
                role.getRoleName(),
                new DateTimeWeekDay((role.getCreatedAt() ==null ? Instant.now() : role.getCreatedAt())),
                role.getPermissions()
        )).toList();
        return ResponseClass.responseSuccess("roles found", "roles", roleResponses);
    }

    public ResponseEntity<?> deleteRole(String hotelId, Long id) {
        Role role = roleRepo.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "role not found"));
        if(!role.getHotelId().equals(hotelId)){ return ResponseClass.responseFailure("invalid request"); }
        role.getPermissions().removeAll(role.getPermissions());
        roleRepo.save(role);
        roleRepo.delete(role);
        return ResponseClass.responseSuccess("role deleted");
    }



    record RoleResponse(Long id, String roleName, DateTimeWeekDay createdAt, Set<Permission> permissions){}


}
