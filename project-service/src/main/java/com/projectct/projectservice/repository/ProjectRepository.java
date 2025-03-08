package com.projectct.projectservice.repository;

import com.projectct.projectservice.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    boolean existsByOwnerIdAndName(Long ownerId, String name);

    List<Project> findByOwnerId(Long ownerId);

    boolean existsByOwnerIdAndNameAndIdNot(Long ownerId, String name, Long id);

    Project findByOwnerUsernameAndName(String ownerUsername, String name);

    @Modifying
    @Query("UPDATE Project p SET p.updatedAt = :updatedAt WHERE p.id = :projectId")
    void updateUpdatedAt(@Param("projectId") Long projectId, @Param("updatedAt") LocalDateTime updatedAt);
}