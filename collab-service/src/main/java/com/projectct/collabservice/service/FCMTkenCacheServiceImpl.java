package com.projectct.collabservice.service;

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
}
