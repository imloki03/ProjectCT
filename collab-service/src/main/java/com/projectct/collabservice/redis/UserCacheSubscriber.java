package com.projectct.collabservice.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectct.collabservice.DTO.User.response.UserResponse;
import com.projectct.collabservice.service.CachedUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCacheSubscriber {
    final CachedUserService cachedUserService;
    final ObjectMapper objectMapper;

    public void handleMessage(String message) {
        UserResponse userResponse = null;
        try {
            userResponse = objectMapper.readValue(message, UserResponse.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        cachedUserService.cacheUser(userResponse);
    }
}
