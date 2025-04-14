package com.projectct.authservice.controller;

import com.projectct.authservice.DTO.RespondData;
import com.projectct.authservice.DTO.User.request.*;
import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.constant.MessageKey;
import com.projectct.authservice.service.UserService;
import com.projectct.authservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("u/{username}")
    public ResponseEntity<?> getUserInfo(@PathVariable String username) {
        var userResponse = userService.getUserInfo(username);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(userResponse)
                .desc(MessageUtil.getMessage(MessageKey.USER_INFO_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping({"i/{userId}"})
    public ResponseEntity<?> getUserInfoById(@PathVariable Long userId) {
        var userResponse = userService.getUserInfoById(userId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(userResponse)
                .desc(MessageUtil.getMessage(MessageKey.USER_INFO_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping({"t"})
    public ResponseEntity<?> getUserInfoViaToken() {
        var userResponse = userService.getUserViaToken();
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

    @PatchMapping("{username}")
    public ResponseEntity<?> changePassword( @RequestBody ChangePasswordRequest request, @PathVariable String username) {
        userService.changePassword(request, username);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.CHANGE_PASSWORD_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> editProfile( @RequestBody EditProfileRequest request) {
        UserResponse res = userService.editProfile(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.EDIT_PROFILE_SUCCESS))
                .data(res)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PatchMapping("status")
    public ResponseEntity<?> updateUserStatus(@RequestBody UpdateStatusRequest request) {
        userService.updateUserStatus(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<?> getUserList(@RequestParam List<Long> userIds) {
        var users = userService.getUserList(userIds);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(users)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PatchMapping("exist/{username}")
    public ResponseEntity<?> checkUserExist(@PathVariable String username) {
        userService.checkUserExist(username);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("tag")
    public ResponseEntity<?> getAllTags() {
        var tags = userService.getAllTags();
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(tags)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("search")
    public ResponseEntity<?> searchUserByUsernameOrEmail(@RequestParam String query) {
        var users = userService.searchUserByUsernameOrEmail(query);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(users)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PutMapping("oauth")
    public ResponseEntity<?> updateOauthUser(@RequestBody UpdateOAuthUserRequest request) {
        UserResponse res = userService.updateOauthUser(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.EDIT_PROFILE_SUCCESS))
                .data(res)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
