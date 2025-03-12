package com.projectct.messageservice.service;

import com.projectct.messageservice.DTO.Message.request.MessageRequest;
import com.projectct.messageservice.DTO.Message.request.PinMessageRequest;
import com.projectct.messageservice.DTO.Message.request.ReadMessageRequest;
import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import com.projectct.messageservice.mapper.MessageMapper;
import com.projectct.messageservice.model.Message;
import com.projectct.messageservice.repository.ChatboxRepository;
import com.projectct.messageservice.repository.MessageRepository;
import com.projectct.messageservice.repository.httpclient.AuthClient;
import com.projectct.messageservice.repository.httpclient.StorageClient;
import com.projectct.messageservice.util.FeignRequestContextUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    final MessageRepository messageRepository;
    final MessageMapper messageMapper;
    final SimpMessagingTemplate messagingTemplate;
    final ChatboxRepository chatboxRepository;
    final AuthClient authClient;
    final StorageClient storageClient;

    @Override
    public void sendMessage(MessageRequest request) {
        Long projectId = request.getProjectId();
        Message message = messageMapper.toMessage(request);
        message.setSentTime(LocalDateTime.now());
        message.setChatbox(chatboxRepository.findByProjectId(projectId));
        messageRepository.save(message);
        MessageResponse messageResponse = fetchFromClients(message, request.getAuthToken());
        messagingTemplate.convertAndSend("/public/project/" + projectId, messageResponse);
    }

    @Override
    public void pinMessage(PinMessageRequest request) {
        Message message = messageRepository.findById(request.getPinMessageId()).orElse(null);
        if (message != null) {
            message.setPinned(!message.isPinned());
            messageRepository.save(message);
            MessageResponse messageResponse = fetchFromClients(message, request.getAuthToken());
            messagingTemplate.convertAndSend("/public/project/" + request.getProjectId(), messageResponse);
        }
    }

    @Override
    public void readMessage(ReadMessageRequest request) {
        String username = request.getUsername();
        Message message = messageRepository.findById(request.getPinMessageId()).orElse(null);
        if (message != null && !message.getReaderList().contains(username)) {
            message.getReaderList().add(username);
            messageRepository.save(message);
        }
        MessageResponse messageResponse = fetchFromClients(message, request.getAuthToken());
        messagingTemplate.convertAndSend("/public/project/" + request.getProjectId(), messageResponse);
    }

    @Override
    public void typingMessage(MessageRequest request) {
        Message message = messageMapper.toMessage(request);
        MessageResponse messageResponse = fetchFromClients(message, request.getAuthToken());
        messagingTemplate.convertAndSend("/public/project/" + request.getProjectId(), messageResponse);
    }

    private MessageResponse fetchFromClients(Message message, String authToken) {
        FeignRequestContextUtil.setAuthToken(authToken);

        MessageResponse response = messageMapper.toMessageResponse(message);
        response.setSender(authClient.getUserInfo(message.getSenderId()).getData());

        if (message.getMediaId() != null) {
            response.setMedia(storageClient.getMediaInfo(message.getMediaId()).getData());
        }
        FeignRequestContextUtil.clear();
        return response;
    }

}
