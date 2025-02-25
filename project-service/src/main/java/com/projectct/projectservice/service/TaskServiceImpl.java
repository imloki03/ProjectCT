package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Task.request.TaskRequest;
import com.projectct.projectservice.DTO.Task.request.UpdateTaskRequest;
import com.projectct.projectservice.DTO.Task.request.UpdateTaskStatusRequest;
import com.projectct.projectservice.DTO.Task.response.TaskResponse;
import com.projectct.projectservice.DTO.Task.response.TaskStatistic;
import com.projectct.projectservice.constant.MessageKey;
import com.projectct.projectservice.enums.Status;
import com.projectct.projectservice.exception.AppException;
import com.projectct.projectservice.mapper.TaskMapper;
import com.projectct.projectservice.model.Backlog;
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

    @Override
    public List<TaskResponse> getAssignedTask(Long collabId) {
        List<Task> tasks = taskRepository.findByAssigneeId(collabId);
        return taskMapper.toTaskResponseList(tasks);
    }

    @Override
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.TASK_NOT_FOUND));
        taskMapper.updateTask(request, task);
        taskRepository.save(task);
        return taskMapper.toTaskResponse(task);
    }

    @Override
    public TaskResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.TASK_NOT_FOUND));
        task.setStatus(request.getStatus());
        if (request.getStatus() == Status.DONE)
            task.setProofList(request.getProofList());
        else
            task.setProofList(new ArrayList<>());
        taskRepository.save(task);
        return taskMapper.toTaskResponse(task);
    }

    public int countTheSameLevelSubTask(Task task){
        if (task.getParentTask() == null)
            return 0;
        return task.getParentTask().getSubTask().size();
    }

    @Transactional
    @Override
    public TaskResponse assignTask(Long taskId, Long collabId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.TASK_NOT_FOUND));
        assignAllSubTask(task, collabId);
        assignToParentTask(task, collabId);
        taskRepository.save(task);
        return taskMapper.toTaskResponse(task);
    }
    public void assignToParentTask(Task task, Long collabId) {
        if (countTheSameLevelSubTask(task) == 1){
            Task parentTask = task.getParentTask();
            parentTask.setAssigneeId(collabId);
            parentTask.setStatus(Status.IN_PROGRESS);
            if (countTheSameLevelSubTask(parentTask) == 1)
                assignToParentTask(task, collabId);
        }
    }
    public void assignAllSubTask(Task task, Long collabId) {
        task.setAssigneeId(collabId);
        task.setStatus(Status.IN_PROGRESS);
        if (task.getSubTask() != null)
            for (Task subTask : task.getSubTask())
                assignAllSubTask(task, collabId);
    }

    @Transactional
    @Override
    public void moveTaskToPhase(Long taskId, Long phaseId) {
        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        if (phase == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.PHASE_NOT_FOUND));
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.TASK_NOT_FOUND));
        if (countTheSameLevelSubTask(task) == 1)
            throw new AppException(HttpStatus.CONFLICT, MessageUtil.getMessage(MessageKey.TASK_MOVE_FAILED));
        task.setParentTask(null);
        moveAllSubtaskToPhase(task, phase);
        taskRepository.save(task);
    }
    public void moveAllSubtaskToPhase(Task task, Phase phase) {
        task.setPhase(phase);
        task.setBacklog(null);
        task.setStatus(Status.TODO);
        if (task.getSubTask() != null)
            for (Task subTask : task.getSubTask())
                moveAllSubtaskToPhase(subTask, phase);
    }

    @Transactional
    @Override
    public void moveTaskToBacklog(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.TASK_NOT_FOUND));
        if (countTheSameLevelSubTask(task) == 1)
            throw new AppException(HttpStatus.CONFLICT, MessageUtil.getMessage(MessageKey.TASK_MOVE_FAILED));
        moveAllSubtaskToBacklog(task);
        taskRepository.save(task);
    }
    public void moveAllSubtaskToBacklog(Task task) {
        Backlog backlog = task.getPhase().getProject().getBacklog();
        task.setBacklog(backlog);
        task.setPhase(null);
        task.setAssigneeId(null);
        task.setProofList(null);
        if (task.getSubTask() != null)
            for (Task subTask : task.getSubTask())
                moveAllSubtaskToBacklog(subTask);
    }

    @Transactional
    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
