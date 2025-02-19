package com.projectct.projectservice.controller;

import com.projectct.projectservice.DTO.RespondData;
import com.projectct.projectservice.DTO.Task.request.TaskRequest;
import com.projectct.projectservice.constant.MessageKey;
import com.projectct.projectservice.service.TaskService;
import com.projectct.projectservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("tasks")
@RequiredArgsConstructor
public class TaskController {
    final TaskService taskService;

    @PostMapping("{projectId}")
    public ResponseEntity<?> createNewTask(@PathVariable Long projectId, @RequestBody TaskRequest request){
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
    public ResponseEntity<?> getTasksInBacklog(@PathVariable Long projectId){
        var tasks = taskService.getTasksInBacklog(projectId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(tasks)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("phase/{phaseId}")
    public ResponseEntity<?> getTasksInPhase(@PathVariable Long phaseId){
        var tasks = taskService.getTasksInPhase(phaseId);
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
}
