package com.example.main.StaffManagement.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.Instant;
import java.util.Set;

@Data
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    private String hotelId;

    private String roleName;

    private Instant createdAt;

    @ManyToMany
    Set<Permission> permissions;

    @JsonIgnore
    @OneToMany
    private Set<Staff> staffs;


}
