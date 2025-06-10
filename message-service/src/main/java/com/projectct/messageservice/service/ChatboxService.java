package com.projectct.messageservice.service;

import com.projectct.messageservice.DTO.Message.response.LastSeenMessageResponse;
import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import com.projectct.messageservice.DTO.Message.response.MessageSourceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatboxService {
    Page<MessageResponse> getOlderMessageByProject(Long projectId, Long lastMessageId, Long taskId, Pageable pageable);

    Page<MessageResponse> getNewerMessageByProject(Long projectId, Long lastMessageId, Long taskId, Pageable pageable);

    Page<MessageResponse> getMediaMessageByProject(Long projectId, Long taskId, Pageable pageable);

    MessageSourceResponse getSourceMessage(Long projectId, Long messageId, Long taskId, Pageable pageable);

    LastSeenMessageResponse getLastSeenMessageByProject(List<String> usernameList, Long projectId, Long taskId);

    Page<MessageResponse> getPinnedMessagesByProject(Long projectId, Long taskId, Pageable pageable);

    Page<MessageResponse> searchMessages(Long projectId, String keyword, String mode, Long taskId, Pageable pageable);
}
