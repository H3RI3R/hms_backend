package com.example.main.StaffManagement.Model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Data
@Entity
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String hotelId;

    private String name;

    private String userName;

    private String email;

    private String address;

    @ManyToOne
    private Role role;

    private String status = "ENABLED";

    private Instant createdAt;

    private Boolean isDeleted = false;

    public void setStatus(String status) {
        switch (status) {
            case "ENABLED", "DISABLED":
                this.status = status;
                break;
            default:
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid status value:" + status);
        }
    }
}
