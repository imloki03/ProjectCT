package com.projectct.projectservice.mapper;

import com.projectct.projectservice.DTO.Task.request.TaskRequest;
import com.projectct.projectservice.DTO.Task.response.TaskResponse;
import com.projectct.projectservice.model.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    @Mapping(source = "parentTask.id", target = "parentTaskId")
    @Mapping(source = "backlog.id", target = "backlogId")
    @Mapping(source = "phase.id", target = "phaseId")
    TaskResponse toTaskResponse(Task task);
    Task toTask(TaskRequest request);
    List<TaskResponse> toTaskResponseList(List<Task> tasks);
}
