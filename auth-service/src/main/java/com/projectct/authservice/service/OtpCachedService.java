package com.projectct.authservice.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

public interface OtpCachedService {
    @Cacheable(value = "otp", key = "#email", unless = "#result == null")
    String getOtp(String email);

    @CacheEvict(value = "otp", key = "#email")
    void clearOtp(String email);
}
