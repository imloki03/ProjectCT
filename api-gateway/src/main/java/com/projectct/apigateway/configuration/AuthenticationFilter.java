package com.projectct.apigateway.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectct.apigateway.DTO.RespondData;
import com.projectct.apigateway.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {
    final AuthService authService;
    final ObjectMapper objectMapper;

    private final List<String> publicEndpoints = List.of(
            "POST:/auth/users/login",
            "POST:/auth/users",
            "GET:/auth/users/exist/**",
            "GET:/auth/otp/**",
            "POST:/auth/otp/verify/**/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Start global filter...");

        if (isPublicEndpoint(exchange.getRequest())){
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate()
                    .headers(headers -> headers.remove(HttpHeaders.AUTHORIZATION))
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }

        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            log.error("No authentication...");
            return unauthenticated(exchange.getResponse());
        }
        String token = authHeader.getFirst().replace("Bearer ", "");
        return authService.verifyToken(token).flatMap(authenticationResponse -> {
            if (authenticationResponse.isAuthenticated())
                return chain.filter(exchange);
            else
                return unauthenticated(exchange.getResponse());
        }).onErrorResume(throwable -> unauthenticated(exchange.getResponse()));
    }

    @Override
    public int getOrder() {
        return -1;
    }

    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private boolean isPublicEndpoint(ServerHttpRequest request) {
        String key = request.getMethod().name() + ":" + request.getURI().getPath();
        return publicEndpoints.stream().anyMatch(pattern -> {
            String[] parts = pattern.split(":", 2);
            return parts[0].equalsIgnoreCase(request.getMethod().name()) && pathMatcher.match(parts[1], request.getURI().getPath());
        });
    }

    Mono<Void> unauthenticated(ServerHttpResponse response) {
        RespondData<?> respondData = RespondData.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .desc("Unauthenticated")
                .build();
        String body = null;
        try {
            body = objectMapper.writeValueAsString(respondData);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        return response.writeWith(Mono.just(response.bufferFactory().wrap(body.getBytes())));
    }
}
