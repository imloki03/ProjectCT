package com.projectct.authservice.service;

import com.projectct.authservice.DTO.Tag.response.TagResponse;
import com.projectct.authservice.DTO.User.request.*;
import com.projectct.authservice.DTO.User.response.LoginResponse;
import com.projectct.authservice.DTO.User.response.UserResponse;

import java.util.List;

public interface UserService {
    void register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    UserResponse getUserInfo(String username);
    void changePassword(ChangePasswordRequest request, String username);
    UserResponse editProfile(EditProfileRequest request);
    void updateUserStatus(UpdateStatusRequest request);
    UserResponse getUserInfoById(Long userId);
    List<UserResponse> getUserList(List<Long> userIds);
    UserResponse getUserViaToken();
    List<TagResponse> getAllTags();
    void checkUserExist(String username);
}
