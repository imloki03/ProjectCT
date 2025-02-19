package com.projectct.authservice.service;

import com.projectct.authservice.DTO.Authentication.AuthenticationResponse;
import com.projectct.authservice.DTO.User.request.*;
import com.projectct.authservice.DTO.User.response.LoginResponse;
import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.exception.AppException;
import com.projectct.authservice.mapper.UserMapper;
import com.projectct.authservice.model.User;
import com.projectct.authservice.model.UserStatus;
import com.projectct.authservice.repository.TagRepository;
import com.projectct.authservice.repository.UserRepository;
import com.projectct.authservice.repository.UserStatusRepository;
import com.projectct.authservice.util.JwtUtil;
import com.projectct.authservice.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.projectct.authservice.constant.MessageKey;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    final UserRepository userRepository;
    final UserMapper userMapper;
    final PasswordEncoder passwordEncoder;
    final JwtUtil jwtUtil;
    final WebUtil webUtil;
    final TagRepository tagRepository;
    final UserStatusRepository userStatusRepository;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(HttpStatus.CONFLICT, MessageKey.EMAIL_ALREADY_EXISTS);
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(HttpStatus.CONFLICT, MessageKey.USERNAME_ALREADY_EXISTS);

        User newUser = userMapper.toUser(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        UserStatus newUserStatus = UserStatus.builder()
                .isNew(true)
                .build();
        userRepository.save(newUser);
        newUserStatus.setUser(newUser);
        userStatusRepository.save(newUserStatus);
        newUser.setStatus(newUserStatus);
        userRepository.save(newUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        var user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername());
        if (user == null)
            throw new AppException(HttpStatus.UNAUTHORIZED, MessageKey.USER_LOGIN_FAILED);
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new AppException(HttpStatus.UNAUTHORIZED, MessageKey.USER_LOGIN_FAILED);

        return LoginResponse.builder()
                .userData(userMapper.toUserResponse(user))
                .token(AuthenticationResponse.builder()
                        .token(jwtUtil.generateAccessToken(user.getUsername()))
                        .authenticated(true)
                        .build())
                .build();
    }

    @Override
    public UserResponse getUserInfo(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        return userMapper.toUserResponse(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        String username = webUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void editProfile(EditProfileRequest request) {
        String username = webUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }

        Optional.ofNullable(request.getName()).ifPresent(user::setName);
        Optional.ofNullable(request.getGender()).ifPresent(user::setGender);
        Optional.ofNullable(request.getAvatarURL()).ifPresent(user::setAvatarURL);

        if (request.getTagList() != null && !request.getTagList().isEmpty()) {
            user.setTagList(tagRepository.findAllById(request.getTagList()));
        }
        userRepository.save(user);
    }

    @Override
    public void activateUser() {
        String username = webUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }

        user.getStatus().setActivated(true);
        userRepository.save(user);
    }

    @Override
    public void introducUser() {
        String username = webUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }

        user.getStatus().setNew(false);
        userRepository.save(user);
    }
}
