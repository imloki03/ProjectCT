package com.projectct.projectservice.controller;

import com.projectct.projectservice.DTO.Project.request.ProjectRequest;
import com.projectct.projectservice.DTO.Project.request.UpdateProjectRequest;
import com.projectct.projectservice.DTO.RespondData;
import com.projectct.projectservice.constant.MessageKey;
import com.projectct.projectservice.service.ProjectService;
import com.projectct.projectservice.util.MessageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController {
    final ProjectService projectService;

    @PostMapping
    public ResponseEntity<?> createNewProject(@RequestBody ProjectRequest request){
        var project = projectService.createNewProject(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(project)
                .desc(MessageUtil.getMessage(MessageKey.PROJECT_CREATE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("{projectId}")
    public ResponseEntity<?> getProject(@PathVariable Long projectId){
        var project = projectService.getProject(projectId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(project)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("{ownerUsername}/{projectName}")
    public ResponseEntity<?> getProjectByOwnerAndName(@PathVariable String ownerUsername, @PathVariable String projectName){
        var project = projectService.getProjectByOwnerAndName(ownerUsername, projectName);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(project)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAllProjects(){
        var projects = projectService.getAllProjects();
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(projects)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @PutMapping("{projectId}")
    public ResponseEntity<?> updateProject(@PathVariable Long projectId, @RequestBody UpdateProjectRequest request){
        var project = projectService.updateProject(projectId, request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .data(project)
                .desc(MessageUtil.getMessage(MessageKey.PROJECT_UPDATE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @DeleteMapping("{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId){
        projectService.deleteProject(projectId);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc(MessageUtil.getMessage(MessageKey.PROJECT_DELETE_SUCCESS))
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
