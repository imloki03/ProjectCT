package com.projectct.authservice.service;



public interface OtpService {
    void sendOtp(String email);
    void verifyOTP(String email, String otp);
}
