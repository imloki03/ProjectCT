package com.projectct.projectservice.model.listener;

import com.projectct.projectservice.configuration.SpringContextHolder;
import com.projectct.projectservice.model.Phase;
import com.projectct.projectservice.model.Project;
import com.projectct.projectservice.model.Task;
import com.projectct.projectservice.repository.ProjectRepository;
import jakarta.persistence.PostPersist;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

public class TaskListener {
    @Transactional
    @PostPersist
    public void updateProjectTimestamp(Task task) {
        Project project = (task.getPhase() != null) ? task.getPhase().getProject() : task.getBacklog().getProject();
        if (project != null) {
            project.setUpdatedAt(LocalDateTime.now());
            ProjectRepository projectRepository = SpringContextHolder.getBean(ProjectRepository.class);
            projectRepository.save(project);
        }
    }
}
