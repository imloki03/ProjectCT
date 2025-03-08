package com.projectct.authservice.service;

import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.constant.MessageKey;
import com.projectct.authservice.exception.AppException;
import com.projectct.authservice.mapper.UserMapper;
import com.projectct.authservice.model.User;
import com.projectct.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserCachedServiceImpl implements UserCachedService {
    final UserRepository userRepository;
    final ApplicationContext applicationContext; // Inject ApplicationContext
    final UserMapper userMapper;

    private UserCachedServiceImpl getSelf() {
        return applicationContext.getBean(UserCachedServiceImpl.class); // Get the proxy bean
    }

    @Cacheable(value = "userInfo", key = "#userId", unless = "#result == null")
    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        return userMapper.toUserResponse(user);
    }

    @Cacheable(value = "userInfo", key = "#result?.id", unless = "#result == null")
    @Override
    public UserResponse getUser(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageKey.USER_NOT_FOUND);
        return userMapper.toUserResponse(user);
    }

    @CachePut(value = "userInfo", key = "#user.id", unless = "#result == null")
    @Override
    public UserResponse updateUser(User user) {
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> getUserList(List<Long> userIds) {
        UserCachedServiceImpl self = getSelf();
        return userIds.stream()
                .map(self::getUserById)
                .toList();
    }
}
