package com.projectct.projectservice.repository.httpclient;

import com.projectct.projectservice.DTO.RespondData;
import com.projectct.projectservice.DTO.User.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service")
public interface AuthClient {
    @GetMapping(value = "/users/{username}", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<UserResponse> getUserInfo(@PathVariable String username);
}
