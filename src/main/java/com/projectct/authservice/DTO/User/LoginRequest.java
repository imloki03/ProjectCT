package com.projectct.authservice.DTO.User;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;
}
