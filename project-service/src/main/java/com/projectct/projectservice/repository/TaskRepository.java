package com.projectct.projectservice.repository;

import com.projectct.projectservice.enums.Status;
import com.projectct.projectservice.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByBacklog_Project_Id(Long id);

    List<Task> findByBacklog_Project_IdAndStatus(Long id, Status status);

    List<Task> findByBacklog_Project_IdOrPhase_Project_Id(Long id, Long id1);
}