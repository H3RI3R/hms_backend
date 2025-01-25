package com.example.main.StaffManagement.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Data
@Entity
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String hotelId;

    private String permissionName;

    @ManyToMany
    private Set<Role> roles;

}
