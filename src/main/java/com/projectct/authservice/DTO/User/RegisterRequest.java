package com.projectct.authservice.DTO.User;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private String username;
    private String name;
    private String password;
    private String email;
    private String gender;
}
