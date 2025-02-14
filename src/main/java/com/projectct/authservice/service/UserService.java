package com.projectct.authservice.service;

import com.projectct.authservice.DTO.User.request.LoginRequest;
import com.projectct.authservice.DTO.User.request.RegisterRequest;
import com.projectct.authservice.DTO.User.response.LoginResponse;
import com.projectct.authservice.DTO.User.response.UserResponse;

public interface UserService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    UserResponse getUserInfo(String username);
}
