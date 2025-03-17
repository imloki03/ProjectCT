package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Media.response.MediaResponse;
import com.projectct.projectservice.DTO.Task.request.TaskRequest;
import com.projectct.projectservice.DTO.Task.request.UpdateTaskRequest;
import com.projectct.projectservice.DTO.Task.request.UpdateTaskStatusRequest;
import com.projectct.projectservice.DTO.Task.response.PageableTaskResponse;
import com.projectct.projectservice.DTO.Task.response.PagingTaskResponse;
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
import com.projectct.projectservice.repository.httpclient.MediaClient;
import com.projectct.projectservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService{
    final TaskRepository taskRepository;
    final PhaseRepository phaseRepository;
    final ProjectRepository projectRepository;
    final TaskMapper taskMapper;
    final MediaClient mediaClient;
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
    public PagingTaskResponse getTasksInBacklog(Long projectId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Order.asc("priority"), Sort.Order.desc("createdDate")));

        Page<Task> taskPage = taskRepository.findByBacklog_Project_IdAndParentTaskIsNull(projectId, pageable);
        long totalTasks = taskRepository.countByBacklog_Project_IdAndParentTaskNull(projectId);

        return PagingTaskResponse.builder()
                .content(taskPage.getContent().stream()
                        .map(this::convertToPageableTaskResponse)
                        .collect(Collectors.toList()))
                .totalTasks(totalTasks)
                .build();
    }

    private PageableTaskResponse convertToPageableTaskResponse(Task task) {
        TaskResponse taskResponse = taskMapper.toTaskResponse(task);

        if (task.getMediaIdList() != null && !task.getMediaIdList().isEmpty()) {
            List<MediaResponse> mediaResponses = mediaClient.getMediaList(task.getMediaIdList()).getData();
            taskResponse.setMediaList(mediaResponses);
        }

        if (task.getProofIdList() != null && !task.getProofIdList().isEmpty()) {
            List<MediaResponse> mediaResponses = mediaClient.getMediaList(task.getProofIdList()).getData();
            taskResponse.setProofList(mediaResponses);
        }

        return PageableTaskResponse.builder()
                .key(task.getId())
                .data(taskResponse)
                .children(task.getSubTask().stream()
                        .map(this::convertToPageableTaskResponse)
                        .collect(Collectors.toList()))
                .build();
    }


    @Override
    public PagingTaskResponse getTasksInPhase(Long phaseId, int page, int size) {
        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        if (phase == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.PHASE_NOT_FOUND));

        Pageable pageable = PageRequest.of(page, size,
                Sort.by(Sort.Order.asc("status"), Sort.Order.desc("createdDate")));

        Page<Task> taskPage = taskRepository.findByPhase_IdAndParentTaskIsNull(phaseId, pageable);

        long totalTasks = taskRepository.countByPhase_IdAndParentTaskNull(phaseId);

        return PagingTaskResponse.builder()
                .content(taskPage.getContent().stream()
                        .map(this::convertToPageableTaskResponse)
                        .collect(Collectors.toList()))
                .totalTasks(totalTasks)
                .build();
    }

    @Override
    public TaskStatistic getTaskStatistic(Long projectId) {
        Long upcoming = (long) taskRepository.findByBacklog_Project_Id(projectId).size();
        Long completed = (long) taskRepository.findByPhase_Project_IdAndStatus(projectId, Status.DONE).size();
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
        List<Task> tasks = taskRepository.findByAssigneeIdAndStatusNot(collabId, Status.DONE);
        return taskMapper.toTaskResponseList(tasks);
    }

    @Override
    public TaskResponse updateTask(Long taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.TASK_NOT_FOUND));
        taskMapper.updateTask(request, task);
        if (request.isRemoveAssignee()) {
            task.setAssigneeId(null);
            if (task.getParentTask() != null)
                unAssignParentTask(task.getParentTask());
        }
        return taskMapper.toTaskResponse(task);
    }

    private void unAssignParentTask(Task task) {
        if (isAllSubtaskNotAssigned(task)){
            task.setAssigneeId(null);
            taskRepository.save(task);
            if (task.getParentTask() != null)
                unAssignParentTask(task.getParentTask());
        }
    }

    private boolean isAllSubtaskNotAssigned(Task task) {
        if (task.getSubTask() == null || task.getSubTask().isEmpty())
            return true;
        for (Task subTask : task.getSubTask()) {
            if (subTask.getAssigneeId() != null) {
                return false;
            } else {
                return isAllSubtaskNotAssigned(subTask);
            }
        }
        return true;
    }

    @Override
    public TaskResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.TASK_NOT_FOUND));
        task.setStatus(request.getStatus());
        if (request.getStatus() == Status.DONE)
            task.setProofIdList(request.getProofList());
        else
            task.setProofIdList(new ArrayList<>());

        if (request.getStatus() == Status.IN_PROGRESS) {
            if (task.getParentTask() != null)
                updateInProgressStatusForParentTask(task.getParentTask());
        } else if (request.getStatus() == Status.DONE) {
            if (task.getParentTask() != null)
                updateDoneStatusForParentTask(task);
        }
        taskRepository.save(task);
        return taskMapper.toTaskResponse(task);
    }

    private void updateInProgressStatusForParentTask(Task task) {
        task.setStatus(Status.IN_PROGRESS);
        if (task.getParentTask() != null)
            updateInProgressStatusForParentTask(task.getParentTask());
    }

    private void updateDoneStatusForParentTask(Task task) {
        if (isAllSubTaskDone(task.getSubTask())) {
            task.setStatus(Status.DONE);
            if (task.getParentTask() != null)
                updateDoneStatusForParentTask(task.getParentTask());
        }
    }

    private boolean isAllSubTaskDone(List<Task> tasks) {
        if (tasks == null || tasks.isEmpty())
            return true;
        for (Task task : tasks)
            if (task.getStatus() != Status.DONE)
                return false;
        return true;
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
                assignToParentTask(parentTask, collabId);
        }
    }
    public void assignAllSubTask(Task task, Long collabId) {
        task.setAssigneeId(collabId);
        task.setStatus(Status.IN_PROGRESS);
        if (task.getSubTask() != null)
            for (Task subTask : task.getSubTask())
                assignAllSubTask(subTask, collabId);
    }

    @Transactional
    @Override
    public void moveTaskToPhase(Long taskId, Long phaseId, UpdateTaskRequest request) {
        updateTask(taskId, request);

        Phase phase = phaseRepository.findById(phaseId).orElse(null);
        if (phase == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.PHASE_NOT_FOUND));
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.TASK_NOT_FOUND));
        if (countTheSameLevelSubTask(task) == 1)
            throw new AppException(HttpStatus.CONFLICT, MessageUtil.getMessage(MessageKey.TASK_MOVE_FAILED));
        task.setParentTask(null);
        moveAllSubtaskToPhase(task, phase, request);
        taskRepository.save(task);
    }
    public void moveAllSubtaskToPhase(Task task, Phase phase, UpdateTaskRequest request) {
        task.setStartTime(request.getStartTime());
        task.setEndTime(request.getEndTime());
        task.setPhase(phase);
        task.setBacklog(null);
        task.setStatus(Status.TODO);
        if (task.getSubTask() != null)
            for (Task subTask : task.getSubTask())
                moveAllSubtaskToPhase(subTask, phase, request);
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
        task.setProofIdList(null);
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
