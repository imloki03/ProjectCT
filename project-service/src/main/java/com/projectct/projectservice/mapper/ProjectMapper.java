package com.projectct.projectservice.mapper;

import com.projectct.projectservice.DTO.Project.request.UpdateProjectRequest;
import com.projectct.projectservice.DTO.Project.response.ProjectResponse;
import com.projectct.projectservice.model.Project;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);
    List<ProjectResponse> toProjectResponseList(List<Project> projects);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProject(UpdateProjectRequest request, @MappingTarget Project project);
}
