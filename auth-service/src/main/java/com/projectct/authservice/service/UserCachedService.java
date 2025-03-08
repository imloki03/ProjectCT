package com.projectct.authservice.service;

import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.model.User;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

public interface UserCachedService {
    @Cacheable(value = "userInfo", key = "#userId", unless = "#result == null")
    UserResponse getUserById(Long userId);

    @CachePut(value = "userInfo", key = "#id", unless = "#result == null")
    UserResponse updateUser(User user);

    List<UserResponse> getUserList(List<Long> userIds);
}
