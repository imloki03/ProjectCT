package com.projectct.collabservice.repository.httpclient;

import com.projectct.collabservice.DTO.RespondData;
import com.projectct.collabservice.DTO.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {
    @GetMapping(value = "/users/u/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<UserResponse> getUserInfo(@PathVariable Long userId);
}
