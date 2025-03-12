package com.projectct.projectservice.service;

import com.projectct.projectservice.DTO.Collab.request.CollabRequest;
import com.projectct.projectservice.DTO.Project.request.ProjectRequest;
import com.projectct.projectservice.DTO.Project.request.UpdateProjectRequest;
import com.projectct.projectservice.DTO.Project.response.ProjectResponse;
import com.projectct.projectservice.constant.KafkaTopic;
import com.projectct.projectservice.constant.MessageKey;
import com.projectct.projectservice.exception.AppException;
import com.projectct.projectservice.mapper.ProjectMapper;
import com.projectct.projectservice.model.Backlog;
import com.projectct.projectservice.model.Project;
import com.projectct.projectservice.repository.ProjectRepository;
import com.projectct.projectservice.repository.httpclient.AuthClient;
import com.projectct.projectservice.repository.httpclient.CollabClient;
import com.projectct.projectservice.util.KafkaProducer;
import com.projectct.projectservice.util.MessageUtil;
import com.projectct.projectservice.util.WebUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService{
    final WebUtil webUtil;
    final ProjectRepository projectRepository;
    final ProjectMapper projectMapper;
    final KafkaProducer kafkaProducer;
    final CollabClient collabClient;

    @Transactional
    @Override
    public ProjectResponse createNewProject(ProjectRequest request) {
        // gọi service khác để tạo media, message....
        Long ownerId = webUtil.getCurrentIdUser();
        String ownerUsername = webUtil.getCurrentUsername();
        if (projectRepository.existsByOwnerIdAndName(ownerId, request.getProjectName()))
            throw new AppException(HttpStatus.CONFLICT, MessageUtil.getMessage(MessageKey.PROJECT_CREATE_DUPLICATE));
        Project project = Project.builder()
                .ownerId(ownerId)
                .ownerUsername(ownerUsername)
                .name(toValidName(request.getProjectName()))
                .description(request.getProjectDescription())
                .build();
        Backlog backlog = Backlog
                .builder()
                .project(project)
                .build();
        project.setBacklog(backlog);
        projectRepository.save(project);
        kafkaProducer.sendMessage(KafkaTopic.INIT_PROJECT, project.getId());

        CollabRequest collabRequest = CollabRequest.builder()
                                    .projectId(project.getId())
                                    .userId(ownerId)
                                    .build();
        kafkaProducer.sendMessage(KafkaTopic.INIT_PROJECT_OWNER_COLLAB, collabRequest);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    public ProjectResponse getProject(Long projectId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.PROJECT_NOT_FOUND));
        return projectMapper.toProjectResponse(project);
    }


    @Override
    public ProjectResponse getProjectByOwnerAndName(String ownerUsername, String projectName) {
        Project project = projectRepository.findByOwnerUsernameAndName(ownerUsername, projectName);
        if (project == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.PROJECT_NOT_FOUND));
        return projectMapper.toProjectResponse(project);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @Override
    public void updateProjectTimestamp(Long projectId) {
        projectRepository.updateUpdatedAt(projectId, LocalDateTime.now());
    }

    @Override
    public List<ProjectResponse> getAllProjects() {
        Long ownerId = webUtil.getCurrentIdUser();
        List<Project> projects = projectRepository.findByOwnerId(ownerId);
        List<Long> collabProject = collabClient.getAllCollabProject(ownerId).getData();
        for (Long projectId : collabProject) {
            projectRepository.findById(projectId).ifPresent(projects::add);
        }
        return projectMapper.toProjectResponseList(projects);
    }

    @Override
    public ProjectResponse updateProject(Long projectId, UpdateProjectRequest request) {
        Project project = projectRepository.findById(projectId).orElse(null);
        Long ownerId = webUtil.getCurrentIdUser();
        if (project == null)
            throw new AppException(HttpStatus.NOT_FOUND, MessageUtil.getMessage(MessageKey.PROJECT_NOT_FOUND));
        if (projectRepository.existsByOwnerIdAndNameAndIdNot(ownerId, request.getName(), projectId))
            throw new AppException(HttpStatus.CONFLICT, MessageUtil.getMessage(MessageKey.PROJECT_CREATE_DUPLICATE));
        projectMapper.updateProject(request, project);
        projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Transactional
    @Override
    public void deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
    }

    private String toValidName(String name) {
        if (name == null || !name.matches("[a-zA-Z0-9\\s]+"))
            throw new AppException(HttpStatus.BAD_REQUEST, MessageKey.PROJECT_NAME_INVALID);
        return name.trim().replaceAll("\\s+", " ");
    }
}
