package com.projectct.messageservice.service;

import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import com.projectct.messageservice.mapper.MessageMapper;
import com.projectct.messageservice.model.Message;
import com.projectct.messageservice.repository.ChatboxRepository;
import com.projectct.messageservice.repository.MessageRepository;
import com.projectct.messageservice.repository.httpclient.AuthClient;
import com.projectct.messageservice.repository.httpclient.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatboxServiceImpl implements ChatboxService {
        final ChatboxRepository chatboxRepository;
        final MessageRepository messageRepository;
        final MessageMapper messageMapper;
        final AuthClient authClient;
        final StorageClient storageClient;


    @Override
    public List<MessageResponse> getPinnedMessagesByProject(Long projectId) {
        List<Message> messages = messageRepository.findByChatbox_ProjectIdAndIsPinnedTrue(projectId);
        return messages.stream().map(this::fetchFromClients).collect(Collectors.toList());
    }

    @Override
    public List<MessageResponse> searchMessages(Long projectId, String keyword) {
        List<Message> messages = messageRepository.findByChatbox_ProjectIdAndContentContains(projectId, keyword);
        return messages.stream().map(this::fetchFromClients).collect(Collectors.toList());
    }

    @Override
    public Page<MessageResponse> getMessagesByProject(Long projectId, Long lastMessageId, Pageable pageable) {
        if (lastMessageId == null || lastMessageId <= 0) {
            lastMessageId = Long.MAX_VALUE;
        }
        Page<Message> messages = messageRepository.findByChatbox_ProjectIdAndIdLessThan(projectId, lastMessageId, pageable);
        return messages.map(this::fetchFromClients);
    }

    //cải tiến
    private MessageResponse fetchFromClients(Message message) {
        MessageResponse response = messageMapper.toMessageResponse(message);
        response.setSender(authClient.getUserInfo(message.getSenderId()).getData());
        if (message.getMediaId() != null) {
            response.setMedia(storageClient.getMediaInfo(message.getMediaId()).getData());
        }
        return response;
    }
}

