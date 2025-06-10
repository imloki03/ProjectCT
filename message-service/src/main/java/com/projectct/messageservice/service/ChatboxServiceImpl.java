package com.projectct.messageservice.service;

import com.projectct.messageservice.DTO.Message.response.LastSeenMessageResponse;
import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import com.projectct.messageservice.DTO.Message.response.MessageSourceResponse;
import com.projectct.messageservice.mapper.MessageMapper;
import com.projectct.messageservice.model.Message;
import com.projectct.messageservice.repository.MessageRepository;
import com.projectct.messageservice.repository.httpclient.AuthClient;
import com.projectct.messageservice.repository.httpclient.StorageClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatboxServiceImpl implements ChatboxService {
    final MessageRepository messageRepository;
    final MessageMapper messageMapper;
    final AuthClient authClient;
    final StorageClient storageClient;

    @Override
    public Page<MessageResponse> getPinnedMessagesByProject(Long projectId, Long taskId, Pageable pageable) {
        long totalElements;
        Page<Message> messages;

        if (taskId != null) {
            totalElements = messageRepository.countByChatbox_TaskIdAndIsPinnedTrue(taskId);
            messages = messageRepository.findByChatbox_TaskIdAndIsPinnedTrue(taskId, pageable);
        } else {
            totalElements = messageRepository.countByChatbox_ProjectIdAndIsPinnedTrue(projectId);
            messages = messageRepository.findByChatbox_ProjectIdAndIsPinnedTrue(projectId, pageable);
        }

        List<MessageResponse> messageResponses = messages.stream()
                .map(this::fetchFromClients)
                .toList();

        return new PageImpl<>(messageResponses, pageable, totalElements);
    }

        @Override
        public Page<MessageResponse> searchMessages(Long projectId, String keyword, String mode, Long taskId, Pageable pageable) {
            Page<Message> messages;
            long totalElements;
    
            if ("media".equalsIgnoreCase(mode)) {
                if (taskId != null) {
                    messages = messageRepository.findByChatbox_TaskIdAndContentContainsAndMediaIdNotNull(taskId, keyword, pageable);
                    totalElements = messageRepository.countByChatbox_TaskIdAndContentContainsAndMediaIdNotNull(taskId, keyword);
                } else {
                    messages = messageRepository.findByChatbox_ProjectIdAndContentContainsAndMediaIdNotNull(projectId, keyword, pageable);
                    totalElements = messageRepository.countByChatbox_ProjectIdAndContentContainsAndMediaIdNotNull(projectId, keyword);
                }
            } else {
                if (taskId != null) {
                    messages = messageRepository.findByChatbox_TaskIdAndContentContainsAndMediaIdNull(taskId, keyword, pageable);
                    totalElements = messageRepository.countByChatbox_TaskIdAndContentContainsAndMediaIdNull(taskId, keyword);
                } else {
                    messages = messageRepository.findByChatbox_ProjectIdAndContentContainsAndMediaIdNull(projectId, keyword, pageable);
                    totalElements = messageRepository.countByChatbox_ProjectIdAndContentContainsAndMediaIdNull(projectId, keyword);
                }
            }
    
            List<MessageResponse> messageResponses = messages.stream()
                    .map(this::fetchFromClients)
                    .toList();
    
            return new PageImpl<>(messageResponses, pageable, totalElements);
        }

    @Override
    public Page<MessageResponse> getOlderMessageByProject(Long projectId, Long lastMessageId, Long taskId, Pageable pageable) {
        LocalDateTime sentTime = LocalDateTime.of(9999, 12, 31, 23, 59, 59);

        if (lastMessageId != 0) {
            Optional<Message> lastMessage = messageRepository.findById(lastMessageId);
            if (lastMessage.isPresent()) {
                sentTime = lastMessage.get().getSentTime();
            }
        }

        Page<Message> messages;
        if (taskId != null) {
            messages = messageRepository.findByChatbox_TaskIdAndSentTimeLessThan(taskId, sentTime, pageable);
        } else {
            messages = messageRepository.findByChatbox_ProjectIdAndSentTimeLessThan(projectId, sentTime, pageable);
        }

        return messages.map(this::fetchFromClients);
    }

    @Override
    public Page<MessageResponse> getNewerMessageByProject(Long projectId, Long lastMessageId, Long taskId, Pageable pageable) {
        LocalDateTime sentTime = messageRepository.findById(lastMessageId).get().getSentTime();

        Page<Message> messages;
        if (taskId != null) {
            messages = messageRepository.findByChatbox_TaskIdAndSentTimeGreaterThan(taskId, sentTime, pageable);
        } else {
            messages = messageRepository.findByChatbox_ProjectIdAndSentTimeGreaterThan(projectId, sentTime, pageable);
        }

        List<MessageResponse> reversedMessages = new ArrayList<>(messages.getContent().stream()
                .map(this::fetchFromClients)
                .toList());
        Collections.reverse(reversedMessages);
        return new PageImpl<>(reversedMessages, pageable, messages.getTotalElements());
    }

    @Override
    public LastSeenMessageResponse getLastSeenMessageByProject(List<String> usernameList, Long projectId, Long taskId) {
        Map<Long, List<String>> lastSeenMap = new HashMap<>();

        for (String username : usernameList) {
            Message message;
            if (taskId != null) {
                message = messageRepository.findFirstByChatbox_TaskIdAndReaderListContainsOrderBySentTimeDesc(taskId, username);
            } else {
                message = messageRepository.findFirstByChatbox_ProjectIdAndReaderListContainsOrderBySentTimeDesc(projectId, username);
            }

            if (message != null) {
                lastSeenMap.computeIfAbsent(message.getId(), k -> new ArrayList<>()).add(username);
            }
        }
        return LastSeenMessageResponse.builder().lastSeenMessageMap(lastSeenMap).build();
    }

    @Override
    public Page<MessageResponse> getMediaMessageByProject(Long projectId, Long taskId, Pageable pageable) {
        long totalElements;
        Page<Message> messages;

        if (taskId != null) {
            totalElements = messageRepository.countByChatbox_TaskIdAndMediaIdNotNull(taskId);
            messages = messageRepository.findByChatbox_TaskIdAndMediaIdNotNull(taskId, pageable);
        } else {
            totalElements = messageRepository.countByChatbox_ProjectIdAndMediaIdNotNull(projectId);
            messages = messageRepository.findByChatbox_ProjectIdAndMediaIdNotNull(projectId, pageable);
        }

        List<MessageResponse> messageResponses = messages.stream()
                .map(this::fetchFromClients)
                .toList();

        return new PageImpl<>(messageResponses, pageable, totalElements);
    }

    @Override
    public MessageSourceResponse getSourceMessage(Long projectId, Long messageId, Long taskId, Pageable pageable) {
        Pageable ascendingSentTime = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("sentTime").ascending());
        LocalDateTime messageTime = messageRepository.findById(messageId).get().getSentTime();

        Page<Message> olderMessages;
        Page<Message> newerMessages;

        if (taskId != null) {
            olderMessages = messageRepository.findByChatbox_TaskIdAndSentTimeLessThan(taskId, messageTime, pageable);
            newerMessages = messageRepository.findByChatbox_TaskIdAndSentTimeGreaterThan(taskId, messageTime, ascendingSentTime);
        } else {
            olderMessages = messageRepository.findByChatbox_ProjectIdAndSentTimeLessThan(projectId, messageTime, pageable);
            newerMessages = messageRepository.findByChatbox_ProjectIdAndSentTimeGreaterThan(projectId, messageTime, ascendingSentTime);
        }

        List<MessageResponse> olderResponses = olderMessages.getContent().stream()
                .map(this::fetchFromClients)
                .toList();

        List<MessageResponse> newerResponses = new ArrayList<>(newerMessages.getContent().stream()
                .map(this::fetchFromClients)
                .toList());
        Collections.reverse(newerResponses);

        newerResponses.add(fetchFromClients(messageRepository.findById(messageId).get()));
        newerResponses.addAll(olderResponses);

        Page<MessageResponse> combine = new PageImpl<>(newerResponses, pageable, newerResponses.size());

        return MessageSourceResponse.builder()
                .messages(combine)
                .hasMoreOlder(!olderMessages.isLast())
                .hasMoreNewer(!newerMessages.isLast())
                .build();
    }

    private MessageResponse fetchFromClients(Message message) {
        MessageResponse response = messageMapper.toMessageResponse(message);
        response.setSender(authClient.getUserInfo(message.getSenderId()).getData());
        if (message.getMediaId() != null) {
            response.setMedia(storageClient.getMediaInfo(message.getMediaId()).getData());
        }
        return response;
    }
}
