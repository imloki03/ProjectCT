package com.projectct.projectservice.mapper;

import com.projectct.projectservice.DTO.Project.response.ProjectResponse;
import com.projectct.projectservice.model.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);
    List<ProjectResponse> toProjectResponseList(List<Project> projects);
}
