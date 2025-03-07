package com.projectct.collabservice.repository.httpclient;

import com.projectct.collabservice.DTO.RespondData;
import com.projectct.collabservice.DTO.User.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "auth-service")
public interface AuthClient {
    @GetMapping(value = "/users/i/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<UserResponse> getUserInfo(@PathVariable Long userId);

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<List<UserResponse>> getUserList(@RequestParam("userIds") List<Long> userIds);
}
