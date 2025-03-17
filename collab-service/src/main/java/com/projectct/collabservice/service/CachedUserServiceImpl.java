package com.projectct.collabservice.service;

import com.projectct.collabservice.DTO.User.response.UserResponse;
import com.projectct.collabservice.repository.httpclient.AuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CachedUserServiceImpl implements CachedUserService{
    final AuthClient authClient;
    final ApplicationContext applicationContext;

    @Cacheable(value = "users", key = "#userId")
    @Override
    public UserResponse getUserInfo(Long userId) {
        return authClient.getUserInfo(userId).getData();
    }

    @Override
    public Map<Long, UserResponse> getUserList(List<Long> userIds) {
        CachedUserServiceImpl self = applicationContext.getBean(CachedUserServiceImpl.class);
        List<Long> missingUserIds = userIds.stream()
                .filter(id -> self.getCachedUser(id) == null)
                .toList();
        if (!missingUserIds.isEmpty()) {
            List<UserResponse> fetchedUsers = authClient.getUserList(missingUserIds).getData();
            fetchedUsers.forEach(self::cacheUser);
        }

        return userIds.stream()
                .map(id -> Map.entry(id, self.getCachedUser(id)))
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Cacheable(value = "users", key = "#userId", unless = "#result == null")
    @Override
    public UserResponse getCachedUser(Long userId) {
        return null;
    }

    @CachePut(value = "users", key = "#user.id", unless = "#result == null")
    @Override
    public UserResponse cacheUser(UserResponse user) {
        return user;
    }
}
