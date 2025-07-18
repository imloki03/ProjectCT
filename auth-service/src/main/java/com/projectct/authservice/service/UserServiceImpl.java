package com.projectct.authservice.service;

import com.projectct.authservice.DTO.Authentication.AuthenticationResponse;
import com.projectct.authservice.DTO.Tag.response.TagResponse;
import com.projectct.authservice.DTO.User.request.*;
import com.projectct.authservice.DTO.User.response.LoginResponse;
import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.constant.KafkaTopic;
import com.projectct.authservice.exception.AppException;
import com.projectct.authservice.mapper.TagMapper;
import com.projectct.authservice.mapper.UserMapper;
import com.projectct.authservice.model.Tag;
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
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    final UserRepository userRepository;
    final UserMapper userMapper;
    final TagMapper tagMapper;
    final PasswordEncoder passwordEncoder;
    final JwtUtil jwtUtil;
    final WebUtil webUtil;
    final TagRepository tagRepository;
    final UserStatusRepository userStatusRepository;
    final UserCachePublisher userCachePublisher;
    final KafkaProducer kafkaProducer;
    final UserCachedService userCachedService;

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
                .newUser(true)
                .activated(false)
                .build();
        userRepository.save(newUser);
        newUserStatus.setUser(newUser);
        userStatusRepository.save(newUserStatus);
        newUser.setStatus(newUserStatus);
        userRepository.save(newUser);
        userCachedService.updateUser(newUser);
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
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }
         return userMapper.toUserResponse(user);
    }

    @Override
    public void changePassword(ChangePasswordRequest request, String username) {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        userCachedService.updateUser(user);
    }

    @Override
    public UserResponse editProfile(EditProfileRequest request) {
        String username = webUtil.getCurrentUsername();
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }

        userMapper.toUpdateUser(request, user);

        if (request.getTagList() != null && !request.getTagList().isEmpty()) {
            user.setTagList(tagRepository.findAllById(request.getTagList()));
        }

        Optional.ofNullable(request.getFcmToken()).ifPresent(token -> {
            if (user.getFcmTokens().contains(token)) {
                user.getFcmTokens().remove(token);
            }
            else {
                user.getFcmTokens().add(token);
            }
        });

        userRepository.save(user);
        userCachedService.updateUser(user);
//      #Collab cache
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

        UserStatus status = user.getStatus();

        if (!status.isActivated()) {
            status.setActivated(request.isActive());
        }

        if (status.isNewUser()) {
            status.setNewUser(request.isNew());
        }

        userRepository.save(user);
        userCachedService.updateUser(user);
    }


    @Override
    public UserResponse getUserInfoById(Long userId) {
        return userCachedService.getUserById(userId);
    }

    @Override
    public List<UserResponse> getUserList(List<Long> userIds) {
        return userIds.stream()
                .map(userCachedService::getUserById)
                .toList();
    }

    @Override
    public UserResponse getUserViaToken() {
        Long userId = webUtil.getCurrentIdUser();
        return userCachedService.getUserById(userId);
    }

    @Override
    public void checkUserExist(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }
    }

    @Override
    public List<UserResponse> searchUserByUsernameOrEmail(String query) {
        if (query.trim().isEmpty()) {
            return null;
        }
        List<User> users = userRepository.findByUsernameContainsOrEmail(query, query);
        return userMapper.toUserResponseList(users.subList(0, Math.min(users.size(), 5)));
    }

    @Transactional
    @Override
    public UserResponse updateOauthUser(UpdateOAuthUserRequest request) {
        if (request.getEmail()!=null && userRepository.existsByEmail(request.getEmail()))
            throw new AppException(HttpStatus.CONFLICT, MessageKey.EMAIL_ALREADY_EXISTS);
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(HttpStatus.CONFLICT, MessageKey.USERNAME_ALREADY_EXISTS);

        Long id = webUtil.getCurrentIdUser();
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        }

        userMapper.toUpdateOauthUser(request, user);

        UserStatus status = user.getStatus();
        if (request.getUsername() != null) {
            status.setActivated(true);
            userStatusRepository.save(status);
        }

        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<TagResponse> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
//        return tagMapper.toTagResponseList(tags);
        return null;
    }
}
