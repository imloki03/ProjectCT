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
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFilter implements GlobalFilter, Ordered {
    final AuthService authService;
    final ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("Start global filter...");
        List<String> authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION);
        if (CollectionUtils.isEmpty(authHeader)) {
            log.error("No authentication...");
//            return unauthenticated(exchange.getResponse());
            return chain.filter(exchange);
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
