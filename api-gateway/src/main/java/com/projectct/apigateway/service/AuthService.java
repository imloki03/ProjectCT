package com.projectct.apigateway.service;

import com.projectct.apigateway.DTO.Authentication.AuthenticationResponse;
import com.projectct.apigateway.DTO.Authentication.TokenRequest;
import com.projectct.apigateway.repository.AuthClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    final AuthClient authClient;
    public Mono<AuthenticationResponse> verifyToken(String token) {
        return authClient.verifyToken(TokenRequest.builder()
                        .token(token)
                        .build());
    }
}
