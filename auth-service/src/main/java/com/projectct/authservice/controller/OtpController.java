package com.projectct.authservice.controller;


import com.projectct.authservice.DTO.RespondData;
import com.projectct.authservice.constant.MessageKey;
import com.projectct.authservice.service.OtpService;
import com.projectct.authservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("otp")
@RequiredArgsConstructor
public class OtpController {
    final OtpService otpService;

    @PostMapping("{email}")
    public ResponseEntity<?> sendOtp(@PathVariable String email){
        otpService.sendOtp(email);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }


    @PostMapping("verify/{email}/{otp}")
    public ResponseEntity<?> verifyOtp(@PathVariable String email, @PathVariable String otp) {
        otpService.verifyOTP(email, otp);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
