package com.projectct.projectservice.repository;

import com.projectct.projectservice.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByOwnerIdAndName(Long ownerId, String name);

    List<Project> findByOwnerId(Long ownerId);

    boolean existsByOwnerIdAndNameAndIdNot(Long ownerId, String name, Long id);
}