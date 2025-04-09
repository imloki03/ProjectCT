package com.projectct.authservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpCachedServiceImpl implements OtpCachedService {

    private String generateOtp() {
        return String.format("%06d", new Random().nextInt(999999));
    }

    @Cacheable(value = "otp", key = "#email", unless = "#result == null")
    @Override
    public String getOtp(String email) {
        return generateOtp();
    }

    @CacheEvict(value = "otp", key = "#email")
    @Override
    public void clearOtp(String email) {}
}
