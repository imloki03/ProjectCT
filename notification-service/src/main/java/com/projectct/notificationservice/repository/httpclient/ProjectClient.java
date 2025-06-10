package com.projectct.notificationservice.repository.httpclient;

import com.projectct.notificationservice.DTO.Collaborator.response.CollabResponse;
import com.projectct.notificationservice.DTO.Project.response.ProjectResponse;
import com.projectct.notificationservice.DTO.RespondData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "project-service")
public interface ProjectClient {
    @GetMapping(value = "/projects/{projectId}", produces = MediaType.APPLICATION_JSON_VALUE)
    RespondData<ProjectResponse> getProject(@PathVariable Long projectId);
}
