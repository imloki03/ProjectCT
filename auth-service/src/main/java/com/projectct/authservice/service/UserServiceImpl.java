package com.projectct.authservice.service;

import com.projectct.authservice.DTO.Authentication.AuthenticationResponse;
import com.projectct.authservice.DTO.User.request.*;
import com.projectct.authservice.DTO.User.response.LoginResponse;
import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.constant.KafkaTopic;
import com.projectct.authservice.exception.AppException;
import com.projectct.authservice.mapper.UserMapper;
import com.projectct.authservice.model.User;
import com.projectct.authservice.model.UserStatus;
import com.projectct.authservice.redis.UserCachePublisher;
import com.projectct.authservice.repository.TagRepository;
import com.projectct.authservice.repository.UserRepository;
import com.projectct.authservice.repository.UserStatusRepository;
import com.projectct.authservice.util.JwtUtil;
import com.projectct.authservice.util.KafkaProducer;
import com.projectct.authservice.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.projectct.authservice.constant.MessageKey;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

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
    final UserCachePublisher userCachePublisher;
    final KafkaProducer kafkaProducer;

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
        kafkaProducer.sendMessage(KafkaTopic.INIT_USER, newUser.getId());
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
    public void changePassword(ChangePasswordRequest request, String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public UserResponse editProfile(EditProfileRequest request) {
        String username = webUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }

        userMapper.toUpdateUser(request, user);

        if (request.getTagList() != null && !request.getTagList().isEmpty()) {
            user.setTagList(tagRepository.findAllById(request.getTagList()));
        }
        userRepository.save(user);

        userCachePublisher.publish(userMapper.toUserResponse(user));

        return userMapper.toUserResponse(user);
    }

    @Override
    public void updateUserStatus(UpdateStatusRequest request) {
        String username = webUtil.getCurrentUsername();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }

        if (!user.getStatus().isActivated())
            user.setStatus(UserStatus.builder().
                        isActivated(request.isActive()).
                        build());

        if (user.getStatus().isNew())
            user.setStatus(UserStatus.builder().
                        isNew(request.isNew())
                        .build());

        userRepository.save(user);
    }

    @Override
    public UserResponse getUserInfoById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getUserList(List<Long> userIds) {
        List<UserResponse> userResponses = new ArrayList<>();
        for (Long userId : userIds)
            userResponses.add(getUserInfoById(userId));
        return userResponses;
    }

    @Override
    public UserResponse getUserViaToken() {
        Long userId = webUtil.getCurrentIdUser();
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        return userMapper.toUserResponse(user);
    }

    @Override
    public void checkUserExist(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
    }
}
