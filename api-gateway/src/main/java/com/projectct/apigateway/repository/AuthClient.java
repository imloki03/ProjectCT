package com.projectct.apigateway.repository;

import com.projectct.apigateway.DTO.Authentication.AuthenticationResponse;
import com.projectct.apigateway.DTO.Authentication.TokenRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import reactor.core.publisher.Mono;

@HttpExchange(url = "lb://auth-service")
public interface AuthClient {
    @PostExchange("/jwt/verify")
    Mono<AuthenticationResponse> verifyToken(@RequestBody TokenRequest request);
}
