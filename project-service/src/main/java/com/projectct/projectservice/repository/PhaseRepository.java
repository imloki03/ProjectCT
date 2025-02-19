package com.projectct.projectservice.repository;

import com.projectct.projectservice.model.Phase;
import com.projectct.projectservice.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhaseRepository extends JpaRepository<Phase, Long> {
    List<Phase> findByProject(Project project);
}