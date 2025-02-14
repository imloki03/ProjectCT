package com.projectct.projectservice.controller;

import com.projectct.projectservice.DTO.Project.request.ProjectRequest;
import com.projectct.projectservice.DTO.RespondData;
import com.projectct.projectservice.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController {
    final ProjectService projectService;

    @PostMapping
    public ResponseEntity<?> createNewProject(@RequestBody ProjectRequest request) {
        projectService.createNewProject(request);
        var respondData = RespondData.builder()
                .status(HttpStatus.OK.value())
                .desc("Create new project successfully!")
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
