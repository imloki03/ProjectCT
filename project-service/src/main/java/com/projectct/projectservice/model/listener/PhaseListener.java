package com.projectct.projectservice.model.listener;

import com.projectct.projectservice.configuration.SpringContextHolder;
import com.projectct.projectservice.model.Phase;
import com.projectct.projectservice.model.Project;
import com.projectct.projectservice.repository.ProjectRepository;
import com.projectct.projectservice.service.ProjectService;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
public class PhaseListener {
    @PostUpdate
    @PostRemove
    public void updateProjectTimestamp(Phase phase) {
        Project project = phase.getProject();
        if (project != null) {
            ProjectService projectService = SpringContextHolder.getBean(ProjectService.class);
            projectService.updateProjectTimestamp(project.getId());
        }
    }
    @Transactional
    @PostPersist
    public void updateProjectTimestamp2(Phase phase) {
        Project project = phase.getProject();
        if (project != null) {
            project.setUpdatedAt(LocalDateTime.now());
            ProjectRepository projectRepository = SpringContextHolder.getBean(ProjectRepository.class);
            projectRepository.save(project);
        }
    }
}