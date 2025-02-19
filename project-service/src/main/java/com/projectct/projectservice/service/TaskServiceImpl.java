package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Task.request.TaskRequest;
import com.projectct.projectservice.DTO.Task.response.TaskResponse;
import com.projectct.projectservice.DTO.Task.response.TaskStatistic;
import com.projectct.projectservice.constant.MessageKey;
import com.projectct.projectservice.enums.Status;
import com.projectct.projectservice.exception.AppException;
import com.projectct.projectservice.mapper.TaskMapper;
import com.projectct.projectservice.model.Phase;
import com.projectct.projectservice.model.Project;
import com.projectct.projectservice.model.Task;
import com.projectct.projectservice.repository.PhaseRepository;
import com.projectct.projectservice.repository.ProjectRepository;
import com.projectct.projectservice.repository.TaskRepository;
import com.projectct.projectservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    final TaskRepository taskRepository;
    final PhaseRepository phaseRepository;
    final ProjectRepository projectRepository;
    final TaskMapper taskMapper;
    @Transactional
    @Override
    public TaskResponse createNewTask(Long projectId, TaskRequest request) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.PROJECT_NOT_FOUND));
        Task task = taskMapper.toTask(request);
        if (request.getParentTaskId() != null){
            Task parentTask = taskRepository.findById(request.getParentTaskId()).orElse(null);
            if (parentTask == null)
                throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.TASK_NOT_FOUND));
            task.setParentTask(parentTask);
            parentTask.addSubTask(task);
            taskRepository.save(parentTask);
        }
        task.setStatus(Status.TODO);
        task.setBacklog(project.getBacklog());
        taskRepository.save(task);
        return taskMapper.toTaskResponse(task);
    }

    @Override
    public TaskResponse getTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.TASK_NOT_FOUND));
        return taskMapper.toTaskResponse(task);
    }

    @Override
    public List<TaskResponse> getTasksInBacklog(Long projectId) {
        List<Task> tasks = taskRepository.findByBacklog_Project_Id(projectId);
        return taskMapper.toTaskResponseList(tasks);
    }

    @Override
    public List<TaskResponse> getTasksInPhase(Long phaseId) {
        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        if (phase == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.PHASE_NOT_FOUND));
        return taskMapper.toTaskResponseList(phase.getTaskList());
    }

    @Override
    public TaskStatistic getTaskStatistic(Long projectId) {
        Long upcoming = (long) getTasksInBacklog(projectId).size();
        Long completed = (long) taskRepository.findByBacklog_Project_IdAndStatus(projectId, Status.DONE).size();
        Long total = (long) taskRepository.findByBacklog_Project_IdOrPhase_Project_Id(projectId, projectId).size();
        Long inProgress = total - completed - upcoming;
        return TaskStatistic.builder()
                .totalTask(total)
                .upcoming(upcoming)
                .inProgress(inProgress)
                .completed(completed)
                .build();
    }
}
