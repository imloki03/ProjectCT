package com.projectct.collabservice.repository;

import com.projectct.collabservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    @Override
    Optional<Role> findById(Long aLong);

    List<Role> findByProjectId(Long projectId);
}