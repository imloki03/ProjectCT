package com.projectct.authservice.controller;

import com.projectct.authservice.DTO.RespondData;
import com.projectct.authservice.DTO.User.request.LoginRequest;
import com.projectct.authservice.DTO.User.request.RegisterRequest;
import com.projectct.authservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {
    final UserService userService;

    @PostMapping
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        userService.register(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc("Register successfully!")
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable String username) {
        var userResponse = userService.getUserInfo(username);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(userResponse)
                .desc("Get user information successfully!")
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var loginResponse = userService.login(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(loginResponse)
                .desc("Login successfully!")
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
