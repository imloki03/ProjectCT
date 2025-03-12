package com.projectct.messageservice.repository;

import com.projectct.messageservice.model.Chatbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatboxRepository extends JpaRepository<Chatbox, Long> {
    Chatbox findByProjectId(Long projectId);
}