package com.projectct.messageservice.service;

import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ChatboxService {
    void createChatBox(Long projectId);

    Page<MessageResponse> getMessagesByProject(Long projectId, int page, int size);

    List<MessageResponse> getPinnedMessagesByProject(Long projectId);

    List<MessageResponse> searchMessages(Long projectId, String keyword);
}
