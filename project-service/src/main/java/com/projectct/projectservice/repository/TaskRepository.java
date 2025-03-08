package com.projectct.projectservice.repository;

import com.projectct.projectservice.enums.Status;
import com.projectct.projectservice.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByBacklog_Project_Id(Long id);

    List<Task> findByBacklog_Project_IdAndStatus(Long id, Status status);

    List<Task> findByBacklog_Project_IdOrPhase_Project_Id(Long id, Long id1);

    List<Task> findByAssigneeId(Long assigneeId);

    Page<Task> findByBacklog_Project_IdAndParentTaskIsNull(Long projectId, Pageable pageable);
    Page<Task> findByPhase_IdAndParentTaskIsNull(Long phaseId, Pageable pageable);

    List<Task> findByPhase_Project_IdAndStatus(Long id, Status status);

    List<Task> findByPhase_Id(Long id);

    long countByPhase_Id(Long id);

    long countByBacklog_Id(Long id);

    long countByBacklog_Project_Id(Long id);
}