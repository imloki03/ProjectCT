package com.projectct.collabservice.repository;

import com.projectct.collabservice.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    Collaborator findByProjectIdAndUserId(Long projectId, Long userId);

    List<Collaborator> findByProjectId(Long projectId);
}