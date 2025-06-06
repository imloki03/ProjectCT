package com.projectct.messageservice.service;

import com.projectct.messageservice.DTO.Message.response.LastSeenMessageResponse;
import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import com.projectct.messageservice.DTO.Message.response.MessageSourceResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatboxService {
    Page<MessageResponse> getPinnedMessagesByProject(Long projectId, Pageable pageable);

    Page<MessageResponse> searchMessages(Long projectId, String keyword, String mode, Pageable pageable);

    Page<MessageResponse> getOlderMessageByProject(Long projectId, Long lastMessageId, Pageable pageable);

    Page<MessageResponse> getMediaMessageByProject(Long projectId, Pageable pageable);

    MessageSourceResponse getSourceMessage(Long projectId, Long messageId, Pageable pageable);

    Page<MessageResponse> getNewerMessageByProject(Long projectId, Long last, Pageable pageable);

    LastSeenMessageResponse getLastSeenMessageByProject(List<String> usernameList, Long projectId);
}
