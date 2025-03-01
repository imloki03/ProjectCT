package com.projectct.collabservice.service;

import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

public interface FCMTokenCacheService {
    @Cacheable(value = "fcmToken", key = "#userId")
    String getFCMToken(Long userId);
}
