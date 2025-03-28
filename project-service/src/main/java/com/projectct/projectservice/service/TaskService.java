package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Task.request.TaskRequest;
import com.projectct.projectservice.DTO.Task.request.UpdateTaskRequest;
import com.projectct.projectservice.DTO.Task.request.UpdateTaskStatusRequest;
import com.projectct.projectservice.DTO.Task.response.*;
import com.projectct.projectservice.enums.Status;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {
    TaskResponse createNewTask(Long projectId, TaskRequest request);
    TaskResponse getTask(Long taskId);
    PagingTaskResponse getTasksInBacklog(Long projectId, int page, int size);
    PagingTaskResponse getTasksInPhase(Long phaseId, int page, int size);
    List<TaskResponse> getAllPhaseTasks(Long projectId);
    TaskStatistic getTaskStatistic(Long projectId);
    List<ProjectAssignedTaskResponse> getAssignedTask();
    TaskResponse updateTask(Long taskId, UpdateTaskRequest request);
    TaskResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request);
    TaskResponse assignTask(Long taskId, Long collabId);
    void moveTaskToPhase(Long taskId, Long phaseId, UpdateTaskRequest request);
    void moveTaskToBacklog(Long taskId);
    void deleteTask(Long taskId);
}
