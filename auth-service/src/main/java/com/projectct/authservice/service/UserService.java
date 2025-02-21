package com.projectct.authservice.service;

import com.projectct.authservice.DTO.User.request.*;
import com.projectct.authservice.DTO.User.response.LoginResponse;
import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.model.User;

public interface UserService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    UserResponse getUserInfo(String username);
    void changePassword(ChangePasswordRequest request);
    UserResponse editProfile(EditProfileRequest request);
    void updateUserStatus(UpdateStatusRequest request);

    UserResponse getUserInfoById(Long userId);
}
