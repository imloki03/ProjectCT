package com.projectct.collabservice.repository.httpclient;

import com.projectct.collabservice.DTO.RespondData;
import com.projectct.collabservice.DTO.User.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "project-service")
public interface TaskClient {
    @GetMapping(value = "/tasks/assigned/{collabId}", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<List<Object>> getAssignedTask(@PathVariable Long collabId);
}
