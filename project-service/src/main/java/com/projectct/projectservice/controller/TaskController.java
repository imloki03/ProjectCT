package com.projectct.projectservice.controller;

import com.projectct.projectservice.DTO.RespondData;
import com.projectct.projectservice.DTO.Task.request.TaskRequest;
import com.projectct.projectservice.DTO.Task.request.UpdateTaskRequest;
import com.projectct.projectservice.DTO.Task.request.UpdateTaskStatusRequest;
import com.projectct.projectservice.constant.MessageKey;
import com.projectct.projectservice.service.TaskService;
import com.projectct.projectservice.util.MessageUtil;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {
    final TaskService taskService;

    @PostMapping("{projectId}")
    public ResponseEntity<?> createNewTask(@PathVariable Long projectId,
                                           @RequestBody TaskRequest request){
        var task = taskService.createNewTask(projectId, request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(task)
                .desc(MessageUtil.getMessage(MessageKey.TASK_CREATE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("{taskId}")
    public ResponseEntity<?> getTask(@PathVariable Long taskId){
        var task = taskService.getTask(taskId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(task)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("backlog/{projectId}")
    public ResponseEntity<?> getTasksInBacklog(@PathVariable Long projectId,
                                               @RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "10") int size){
        var tasks = taskService.getTasksInBacklog(projectId, page, size);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(tasks)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("phase/{phaseId}")
    public ResponseEntity<?> getTasksInPhase(@PathVariable Long phaseId,
                                             @RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "10") int size){
        var tasks = taskService.getTasksInPhase(phaseId, page, size);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(tasks)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("stat/{projectId}")
    public ResponseEntity<?> getTaskStatistic(@PathVariable Long projectId){
        var stat = taskService.getTaskStatistic(projectId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(stat)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("assigned/{collabId}")
    public ResponseEntity<?> getAssignedTask(@PathVariable Long collabId){
        var tasks = taskService.getAssignedTask(collabId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(tasks)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PutMapping("{taskId}")
    public ResponseEntity<?> updateTask(@PathVariable Long taskId,
                                        @RequestBody UpdateTaskRequest request){
        var task = taskService.updateTask(taskId, request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(task)
                .desc(MessageUtil.getMessage(MessageKey.TASK_UPDATE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PatchMapping("{taskId}")
    public ResponseEntity<?> updateTaskStatus(@PathVariable Long taskId,
                                              @RequestBody UpdateTaskStatusRequest request){
        var task = taskService.updateTaskStatus(taskId, request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(task)
                .desc(MessageUtil.getMessage(MessageKey.TASK_UPDATE_STATUS_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PatchMapping("assign/{taskId}/{collabId}")
    public ResponseEntity<?> assignTask(@PathVariable Long taskId, @PathVariable Long collabId){
        var task = taskService.assignTask(taskId, collabId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(task)
                .desc(MessageUtil.getMessage(MessageKey.TASK_ASSIGN_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PatchMapping("move/{taskId}/{phaseId}")
    public ResponseEntity<?> moveTaskToPhase(@PathVariable Long taskId, @PathVariable Long phaseId){
        taskService.moveTaskToPhase(taskId, phaseId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.TASK_MOVE_PHASE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PatchMapping("move/{taskId}")
    public ResponseEntity<?> moveTaskToBacklog(@PathVariable Long taskId){
        taskService.moveTaskToBacklog(taskId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.TASK_MOVE_BACKLOG_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @DeleteMapping("{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable Long taskId){
        taskService.deleteTask(taskId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.TASK_DELETE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
