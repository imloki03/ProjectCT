package com.projectct.messageservice.repository.httpclient;
import com.projectct.messageservice.DTO.RespondData;
import com.projectct.messageservice.DTO.User.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "auth-service")
public interface AuthClient {
    @GetMapping(value = "/users/i/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<UserResponse> getUserInfo(@PathVariable Long userId);
}
