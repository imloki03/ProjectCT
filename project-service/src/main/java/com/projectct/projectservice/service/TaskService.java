package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Task.request.TaskRequest;
import com.projectct.projectservice.DTO.Task.response.TaskResponse;
import com.projectct.projectservice.DTO.Task.response.TaskStatistic;

import java.util.List;

public interface TaskService {
    TaskResponse createNewTask(Long projectId, TaskRequest request);
    TaskResponse getTask(Long taskId);
    List<TaskResponse> getTasksInBacklog(Long projectId);
    List<TaskResponse> getTasksInPhase(Long phaseId);
    TaskStatistic getTaskStatistic(Long projectId);
}
