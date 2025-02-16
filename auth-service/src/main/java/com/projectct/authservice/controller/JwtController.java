package com.projectct.authservice.controller;

import com.projectct.authservice.DTO.Authentication.AuthenticationResponse;
import com.projectct.authservice.DTO.Authentication.TokenRequest;
import com.projectct.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("jwt")
@RequiredArgsConstructor
public class JwtController {
    final JwtUtil jwtUtil;

    @PostMapping("verify")
    public ResponseEntity<?> verifyToken(@RequestBody TokenRequest request) {
        boolean valid = jwtUtil.isValidToken(request.getToken());
        var respondData = AuthenticationResponse.builder()
                .authenticated(valid)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PostMapping("refresh")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRequest request) {
        var token = jwtUtil.refreshToken(request.getToken());
        var respondData = AuthenticationResponse.builder()
                .token(token.getToken())
                .authenticated(true)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
