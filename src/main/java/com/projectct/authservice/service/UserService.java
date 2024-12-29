package com.projectct.authservice.service;

import com.projectct.authservice.DTO.User.LoginRequest;
import com.projectct.authservice.DTO.User.RegisterRequest;
import com.projectct.authservice.DTO.User.UserResponse;

public interface UserService {
    void register(RegisterRequest request);
    UserResponse login(LoginRequest request);
    UserResponse getUserInfo(String username);
}
