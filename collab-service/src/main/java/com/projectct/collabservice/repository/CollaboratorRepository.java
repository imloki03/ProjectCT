package com.projectct.collabservice.repository;

import com.projectct.collabservice.model.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    Collaborator findByProjectIdAndUserId(Long projectId, Long userId);

    List<Collaborator> findByProjectId(Long projectId);

    List<Collaborator> findByUserId(Long userId);

    List<Collaborator> findByUserIdAndRole_NameNot(Long userId, String name);

    boolean existsByRole_Id(Long id);
}