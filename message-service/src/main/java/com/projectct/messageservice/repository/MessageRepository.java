package com.projectct.messageservice.repository;

import com.projectct.messageservice.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByChatbox_ProjectId(Long projectId, Pageable pageable);

    List<Message> findByChatbox_ProjectIdAndContentContains(Long projectId, String content);

    Page<Message> findByChatbox_ProjectIdAndIdLessThan(Long projectId, Long id, Pageable pageable);

    Page<Message> findByChatbox_ProjectIdAndMediaIdNotNull(Long projectId, Pageable pageable);

    long countByChatbox_ProjectIdAndMediaIdNotNull(Long projectId);

    Page<Message> findByChatbox_ProjectIdAndIdGreaterThan(Long projectId, Long id, Pageable pageable);

    Page<Message> findByChatbox_ProjectIdAndSentTimeLessThan(Long projectId, LocalDateTime sentTime, Pageable pageable);

    Page<Message> findByChatbox_ProjectIdAndSentTimeGreaterThan(Long projectId, LocalDateTime sentTime, Pageable pageable);

    Message findFirstByChatbox_ProjectIdAndReaderListContainsOrderBySentTimeDesc(Long projectId, String readerList);

    long countByChatbox_ProjectIdAndIsPinnedTrue(Long projectId);

    Page<Message> findByChatbox_ProjectIdAndIsPinnedTrueOrderBySentTimeDesc(Long projectId, Pageable pageable);

    Page<Message> findByChatbox_ProjectIdAndIsPinnedTrue(Long projectId, Pageable pageable);

    Page<Message> findByChatbox_ProjectIdAndContentContainsAndMediaIdNotNull(Long projectId, String content, Pageable pageable);

    Page<Message> findByChatbox_ProjectIdAndContentContainsAndMediaIdNull(Long projectId, String content, Pageable pageable);

    long countByChatbox_ProjectIdAndContentContainsAndMediaIdNotNull(Long projectId, String content);

    long countByChatbox_ProjectIdAndContentContainsAndMediaIdNull(Long projectId, String content);

    Page<Message> findByChatbox_ProjectIdAndChatbox_TaskIdAndSentTimeLessThan(Long projectId, Long taskId, LocalDateTime sentTime, Pageable pageable);

    // For taskId
    Page<Message> findByChatbox_TaskIdAndIsPinnedTrue(Long taskId, Pageable pageable);
    long countByChatbox_TaskIdAndIsPinnedTrue(Long taskId);


    Page<Message> findByChatbox_TaskIdAndContentContainsAndMediaIdNull(Long taskId, String keyword, Pageable pageable);
    long countByChatbox_TaskIdAndContentContainsAndMediaIdNull(Long taskId, String keyword);

    Page<Message> findByChatbox_TaskIdAndContentContainsAndMediaIdNotNull(Long taskId, String keyword, Pageable pageable);
    long countByChatbox_TaskIdAndContentContainsAndMediaIdNotNull(Long taskId, String keyword);


    Page<Message> findByChatbox_TaskIdAndSentTimeLessThan(Long taskId, LocalDateTime sentTime, Pageable pageable);
    Page<Message> findByChatbox_TaskIdAndSentTimeGreaterThan(Long taskId, LocalDateTime sentTime, Pageable pageable);


    Page<Message> findByChatbox_TaskIdAndMediaIdNotNull(Long taskId, Pageable pageable);
    long countByChatbox_TaskIdAndMediaIdNotNull(Long taskId);

    Message findFirstByChatbox_TaskIdAndReaderListContainsOrderBySentTimeDesc(Long taskId, String username);
}