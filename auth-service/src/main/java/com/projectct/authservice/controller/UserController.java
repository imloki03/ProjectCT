package com.projectct.authservice.controller;

import com.projectct.authservice.DTO.RespondData;
import com.projectct.authservice.DTO.User.request.*;
import com.projectct.authservice.constant.MessageKey;
import com.projectct.authservice.service.UserService;
import com.projectct.authservice.util.MessageUtil;
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
                .desc(MessageUtil.getMessage(MessageKey.USER_REGISTER_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable String username) {
        var userResponse = userService.getUserInfo(username);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(userResponse)
                .desc(MessageUtil.getMessage(MessageKey.USER_INFO_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var loginResponse = userService.login(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(loginResponse)
                .desc(MessageUtil.getMessage(MessageKey.USER_LOGIN_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PatchMapping
    public ResponseEntity<?> changePassword( @RequestBody ChangePasswordRequest request) {
        userService.changePassword(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.CHANGE_PASSWORD_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> editProfile( @RequestBody EditProfileRequest request) {
        userService.editProfile(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.EDIT_PROFILE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PutMapping("avatar")
    public ResponseEntity<?> editProfileAvatar( @RequestBody EditUserAvatarRequest request) {
        userService.editProfileAvatar(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.EDIT_PROFILE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PatchMapping("activate")
    public ResponseEntity<?> activateUser() {
        userService.activateUser();
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.USER_ACTIVATE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PatchMapping("introduce")
    public ResponseEntity<?> introduceUser( ) {
        userService.introducUser();
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.REQUEST_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
