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

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService{
    final KafkaProducer kafkaProducer;
    final OtpCachedServiceImpl otpCachedService;

    @Override
    public void sendOtp(String email) {
        String otp = otpCachedService.getOtp(email);
        if (Optional.ofNullable(otp).isPresent()) {
           otpCachedService.clearOtp(email);
           otp = otpCachedService.getOtp(email);
        }
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
        String storedOtp = otpCachedService.getOtp(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpCachedService.clearOtp(email);
            return;
        }
        throw new AppException(HttpStatus.BAD_REQUEST, MessageKey.OTP_INVALID);
    }
}
