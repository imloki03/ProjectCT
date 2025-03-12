package com.projectct.messageservice.repository;

import com.projectct.messageservice.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatbox_ProjectId(Long projectId, Pageable pageable);

    List<Message> findByChatbox_ProjectIdAndIsPinnedTrue(Long projectId);

    List<Message> findByChatbox_ProjectIdAndContentContains(Long projectId, String content);

    Page<Message> findByChatbox_ProjectIdAndIdLessThan(Long projectId, Long id, Pageable pageable);
}