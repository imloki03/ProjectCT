package com.projectct.messageservice.service;

import com.projectct.messageservice.DTO.Message.request.MessageRequest;
import com.projectct.messageservice.DTO.Message.request.PinMessageRequest;
import com.projectct.messageservice.DTO.Message.request.ReadMessageRequest;
import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import com.projectct.messageservice.mapper.MessageMapper;
import com.projectct.messageservice.model.Message;
import com.projectct.messageservice.repository.MessageRepository;
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

    @Override
    public void sendMessage(MessageRequest request) {
        Message message = messageMapper.toMessage(request);
        message.setSentTime(LocalDateTime.now());
        messageRepository.save(message);
        MessageResponse messageResponse = messageMapper.toMessageResponse(message);
        messagingTemplate.convertAndSend("/public/project/" + request.getProjectId(), messageResponse);
    }

    @Override
    public void pinMessage(PinMessageRequest request) {
        Message message = messageRepository.findById(request.getPinMessageId()).orElse(null);
        message.setPinned(!message.isPinned());
        messageRepository.save(message);
        MessageResponse messageResponse = messageMapper.toMessageResponse(message);
        messagingTemplate.convertAndSend("/public/project/" + request.getProjectId(), messageResponse);
    }

    @Override
    public void readMessage(ReadMessageRequest request) {
        String username = request.getUsername();
        Message message = messageRepository.findById(request.getPinMessageId()).orElse(null);
        if (!message.getReaderList().contains(username)) {
            message.getReaderList().add(username);
            messageRepository.save(message);
        }
        MessageResponse messageResponse = messageMapper.toMessageResponse(message);
        messagingTemplate.convertAndSend("/public/project/" + request.getProjectId(), messageResponse);
    }

    @Override
    public void typingMessage(MessageRequest request) {
        Message message = messageMapper.toMessage(request);
        MessageResponse messageResponse = messageMapper.toMessageResponse(message);
        messagingTemplate.convertAndSend("/public/project/" + request.getProjectId(), messageResponse);
    }
}
