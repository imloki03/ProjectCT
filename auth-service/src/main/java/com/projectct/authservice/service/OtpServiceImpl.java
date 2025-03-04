package com.projectct.authservice.service;

import com.projectct.authservice.DTO.Email.request.EmailRequest;
import com.projectct.authservice.constant.HTMLTemplate;
import com.projectct.authservice.constant.KafkaTopic;
import com.projectct.authservice.constant.MessageKey;
import com.projectct.authservice.exception.AppException;
import com.projectct.authservice.util.KafkaProducer;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService{
    final KafkaProducer kafkaProducer;

    private final Map<String, String> otpStorage = new HashMap<>();  //use redis instead map
    @Override
    public String generateOtp(String email) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStorage.put(email, otp);
        return otp;
    }

    @Override
    public void sendOtp(String email) {
        String otp = generateOtp(email);
        String subject = "OTP Verification";
        kafkaProducer.sendMessage(KafkaTopic.SEND_EMAIL , EmailRequest.builder()
                        .receiver(email)
                        .subject(subject)
                        .templateName(HTMLTemplate.OTP_TEMPLATE)
                        .args(List.of(otp))
                .build());
    }

    @Override
    public void verifyOTP(String email, String otp) {
        String storedOtp = otpStorage.get(email);
        log.error(otpStorage.toString());
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStorage.remove(email);
            return;
        }
        throw new AppException(HttpStatus.BAD_REQUEST, MessageKey.OTP_INVALID);
    }
}
