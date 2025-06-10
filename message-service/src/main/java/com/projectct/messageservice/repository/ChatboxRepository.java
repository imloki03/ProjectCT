package com.projectct.messageservice.repository;

import com.projectct.messageservice.model.Chatbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatboxRepository extends JpaRepository<Chatbox, Long> {
    Chatbox findByProjectId(Long projectId);

    Optional<Chatbox> findByProjectIdAndTaskId(Long projectId, Long taskId);
}