package com.projectct.notificationservice.service;

import com.projectct.notificationservice.DTO.FCMToken.request.FCMTokenRequest;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public interface FCMTokenCacheService {
    @Cacheable(value = "fcmToken", key = "#userId")
    String getFCMToken(Long userId);

    @CachePut(value = "fcmToken", key = "#token.userId", unless = "#result == null")
    String cacheFCMToken(FCMTokenRequest token);
}
