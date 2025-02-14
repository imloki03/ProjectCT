package com.projectct.projectservice.repository.httpclient;

import com.projectct.projectservice.DTO.User.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service", url = "http://localhost:8080/auth")
public interface AuthClient {
    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    UserResponse getUserInfo(@PathVariable String username);
}
