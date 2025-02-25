package com.projectct.collabservice.service;

import com.projectct.collabservice.DTO.User.response.UserResponse;

import java.util.List;
import java.util.Map;

public interface CachedUserService {
    UserResponse getUserInfo(Long userId);
    Map<Long, UserResponse> getUserList(List<Long> userIds);
    UserResponse getCachedUser(Long userId);
    UserResponse cacheUser(UserResponse user);
}
