package com.projectct.authservice.service;

import com.projectct.authservice.DTO.User.LoginRequest;
import com.projectct.authservice.DTO.User.RegisterRequest;
import com.projectct.authservice.DTO.User.UserResponse;
import com.projectct.authservice.exception.AppException;
import com.projectct.authservice.mapper.UserMapper;
import com.projectct.authservice.model.User;
import com.projectct.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    final UserRepository userRepository;
    final UserMapper userMapper;
    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "Email has been used!");
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(HttpStatus.BAD_REQUEST.value(), "Username is currently existed!");

        User newUser = userMapper.toUser(request);
        userRepository.save(newUser);
    }

    @Override
    public UserResponse login(LoginRequest request) {
        return null;
    }

    @Override
    public UserResponse getUserInfo(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null)
            throw new AppException(HttpStatus.NOT_FOUND.value(), "User not found");
        return userMapper.toUserResponse(user);
    }
}
