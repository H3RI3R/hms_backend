package com.example.main.Login.DTO;

import lombok.Data;

@Data
public class LoginDTO {

    private String email;

    private String password;

    private boolean rememberMe = false;
}
