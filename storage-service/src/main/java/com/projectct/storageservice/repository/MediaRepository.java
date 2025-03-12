package com.projectct.storageservice.repository;

import com.projectct.storageservice.model.Media;
import com.projectct.storageservice.model.Storage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MediaRepository extends JpaRepository<Media, Long> {
    Media findByPreviousVersion_Id(Long id);

    List<Media> findByStorageAndNewerVersionNull(Storage storage, Pageable pageable);

    long countByStorageAndPreviousVersionNull(Storage storage);
}