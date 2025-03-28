package com.projectct.projectservice.DTO.Task.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.projectct.projectservice.DTO.Project.response.ProjectResponse;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectAssignedTaskResponse {
    ProjectResponse project;
    List<TaskResponse> taskList;
}
