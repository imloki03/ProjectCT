package com.projectct.messageservice.service;

import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatboxService {
    List<MessageResponse> getPinnedMessagesByProject(Long projectId);

    List<MessageResponse> searchMessages(Long projectId, String keyword);

    Page<MessageResponse> getMessagesByProject(Long projectId, Long lastMessageId, Pageable pageable);
}
