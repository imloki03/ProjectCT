package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Task.request.TaskRequest;
import com.projectct.projectservice.DTO.Task.request.UpdateTaskRequest;
import com.projectct.projectservice.DTO.Task.request.UpdateTaskStatusRequest;
import com.projectct.projectservice.DTO.Task.response.TaskResponse;
import com.projectct.projectservice.DTO.Task.response.TaskStatistic;
import com.projectct.projectservice.enums.Status;

import java.util.List;

public interface TaskService {
    TaskResponse createNewTask(Long projectId, TaskRequest request);
    TaskResponse getTask(Long taskId);
    List<TaskResponse> getTasksInBacklog(Long projectId);
    List<TaskResponse> getTasksInPhase(Long phaseId);
    TaskStatistic getTaskStatistic(Long projectId);
    List<TaskResponse> getAssignedTask(Long collabId);
    TaskResponse updateTask(Long taskId, UpdateTaskRequest request);
    TaskResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request);
    TaskResponse assignTask(Long taskId, Long collabId);
    void moveTaskToPhase(Long taskId, Long phaseId);
    void moveTaskToBacklog(Long taskId);
    void deleteTask(Long taskId);
}
