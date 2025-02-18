package com.projectct.authservice.service;

import com.projectct.authservice.DTO.Authentication.AuthenticationResponse;
import com.projectct.authservice.DTO.User.request.LoginRequest;
import com.projectct.authservice.DTO.User.request.RegisterRequest;
import com.projectct.authservice.DTO.User.response.LoginResponse;
import com.projectct.authservice.DTO.User.response.UserResponse;
import com.projectct.authservice.exception.AppException;
import com.projectct.authservice.mapper.UserMapper;
import com.projectct.authservice.model.User;
import com.projectct.authservice.repository.UserRepository;
import com.projectct.authservice.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    final UserRepository userRepository;
    final UserMapper userMapper;
    final PasswordEncoder passwordEncoder;
    final JwtUtil jwtUtil;
    @Override
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has been used!");
        if (userRepository.existsByUsername(request.getUsername()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Username is currently existed!");

        User newUser = userMapper.toUser(request);
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));

        userRepository.save(newUser);
    }

    @Override
    public LoginResponse login(LoginRequest request) {
        var user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername());
        if (user == null)
            throw new AppException(HttpStatus.UNAUTHORIZED, "User not found");
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new AppException(HttpStatus.UNAUTHORIZED, "Password is incorrect");

        return LoginResponse.builder()
                .userData(userMapper.toUserResponse(user))
                .token(AuthenticationResponse.builder()
                        .token(jwtUtil.generateAccessToken(request.getUsername()))
                        .authenticated(true)
                        .build())
                .build();
    }

    @Override
    public UserResponse getUserInfo(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username);
        if (user == null)
            throw new AppException(HttpStatus.NOT_FOUND, "User not found");
        return userMapper.toUserResponse(user);
    }
}
