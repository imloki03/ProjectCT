package com.projectct.projectservice.repository.httpclient;

import com.projectct.projectservice.DTO.Collaborator.response.CollabResponse;
import com.projectct.projectservice.DTO.RespondData;
import com.projectct.projectservice.DTO.User.response.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "collab-service")
public interface CollabClient {
    @GetMapping(value = "/collabs/collab/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<List<Long>> getAllCollabProject(@PathVariable Long userId);

    @GetMapping(value = "/collabs/cid/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<List<Long>> getAllCollabIdList(@PathVariable Long userId);

    @GetMapping(value = "/collabs/{collabId}", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<CollabResponse> getCollabById(@PathVariable("collabId") Long collabId);
}
