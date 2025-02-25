package com.projectct.authservice.redis;

import com.projectct.authservice.DTO.User.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserCachePublisher {
    final RedisTemplate<String, Object> redisTemplate;
    private static final String CHANNEL = "user-update";

    public void publish(UserResponse userResponse) {
        redisTemplate.convertAndSend(CHANNEL, userResponse);
    }
}
