package com.projectct.storageservice.repository;

import com.projectct.storageservice.model.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage, Long> {
    Storage findByProjectId(Long projectId);
}