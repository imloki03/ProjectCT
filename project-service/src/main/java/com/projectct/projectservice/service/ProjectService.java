package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Project.request.ProjectRequest;
import com.projectct.projectservice.DTO.Project.response.ProjectResponse;

public interface ProjectService {
    ProjectResponse createNewProject(ProjectRequest request);
}
