package com.example.main.StaffManagement.DTO;

import lombok.Data;

@Data
public class StaffDTO {
    private String name;
    private String userName;
    private String email;
    private String roleName;
    private String status;
//    private String password;
    private Long roleId;
    private String address;
}
