package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Project.request.ProjectRequest;
import com.projectct.projectservice.DTO.Project.request.UpdateProjectRequest;
import com.projectct.projectservice.DTO.Project.response.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse createNewProject(ProjectRequest request);
    ProjectResponse getProject(Long projectId);
    List<ProjectResponse> getAllProjects();
    ProjectResponse updateProject(Long projectId, UpdateProjectRequest request);
    void deleteProject(Long projectId);
    ProjectResponse getProjectByOwnerAndName(String ownerUsername, String projectName);
}
