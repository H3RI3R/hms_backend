package com.example.main.StaffManagement.DTO;

import lombok.Data;

import java.util.List;

@Data
public class CreateRoleDTO {

    private String roleName;

    private List<String> pagePermissions;

    private List<String> removePermissions;

}
