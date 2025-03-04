package com.projectct.authservice.service;



public interface OtpService {
    String generateOtp(String email);
    void sendOtp(String email);
    void verifyOTP(String email, String otp);
}
