package com.projectct.notificationservice.service;

import com.projectct.notificationservice.DTO.FCMToken.request.FCMTokenRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class FCMTkenCacheServiceImpl implements FCMTokenCacheService{

    @Cacheable(value = "fcmToken", key = "#userId", unless = "#result == null")
    @Override
    public String getFCMToken(Long userId) {
        return null;
    }

    @CachePut(value = "fcmToken", key = "#request.userId", unless = "#result == null")
    @Override
    public String cacheFCMToken(FCMTokenRequest request) {
        return request.getToken();
    }
}
