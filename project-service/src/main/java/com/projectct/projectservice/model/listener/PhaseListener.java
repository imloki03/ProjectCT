package com.projectct.projectservice.model.listener;

import com.projectct.projectservice.configuration.SpringContextHolder;
import com.projectct.projectservice.model.Phase;
import com.projectct.projectservice.model.Project;
import com.projectct.projectservice.repository.ProjectRepository;
import jakarta.persistence.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
public class PhaseListener {
    @PostPersist
    @PostUpdate
    @PostRemove
    @Transactional
    public void updateProjectTimestamp(Phase phase) {
        Project project = phase.getProject();
        log.error("in");
        if (project != null) {
            project.setUpdatedAt(LocalDateTime.now());
            log.error(project.getUpdatedAt().toString());
            ProjectRepository projectRepository = SpringContextHolder.getBean(ProjectRepository.class);
            projectRepository.save(project);
        }
    }
}