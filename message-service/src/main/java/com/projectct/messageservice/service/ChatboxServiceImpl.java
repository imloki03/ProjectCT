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
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatboxServiceImpl implements ChatboxService {
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
    public Page<MessageResponse> getOlderMessageByProject(Long projectId, Long lastMessageId, Pageable pageable) {
        LocalDateTime sentTime =  LocalDateTime.of(9999, 12, 31, 23, 59, 59);

        if (lastMessageId != 0) {
            Optional<Message> lastMessage = messageRepository.findById(lastMessageId);
            if (lastMessage.isPresent()) {
                sentTime = lastMessage.get().getSentTime();
            }
        }

        Page<Message> messages = messageRepository.findByChatbox_ProjectIdAndSentTimeLessThan(projectId, sentTime, pageable);
        return messages.map(this::fetchFromClients);
    }

    @Override
    public Page<MessageResponse> getNewerMessageByProject(Long projectId, Long lastMessageId, Pageable pageable) {
        Page<Message> messages = messageRepository.findByChatbox_ProjectIdAndSentTimeGreaterThan(projectId, messageRepository.findById(lastMessageId).get().getSentTime(), pageable);
        List<MessageResponse> reversedMessages = new ArrayList<>(messages.getContent().stream()
                .map(this::fetchFromClients)
                .toList());
        Collections.reverse(reversedMessages);
        return new PageImpl<>(reversedMessages, pageable, messages.getTotalElements());
    }

    @Override
    public LastSeenMessageResponse getLastSeenMessageByProject(List<String> usernameList, Long projectId) {
        Map<Long, List<String>> lastSeenMap = new HashMap<>();

        for (String username : usernameList) {
            Message message = messageRepository.findFirstByChatbox_ProjectIdAndReaderListContainsOrderBySentTimeDesc(projectId, username);

            if (message != null) {
                lastSeenMap.computeIfAbsent(message.getId(), k -> new ArrayList<>()).add(username);
            }
        }
        return LastSeenMessageResponse.builder().lastSeenMessageMap(lastSeenMap).build();
    }


    @Override
    public Page<MessageResponse> getMediaMessageByProject(Long projectId, Pageable pageable) {
        long totalElements = messageRepository.countByChatbox_ProjectIdAndMediaIdNotNull(projectId);
        Page<Message> messages = messageRepository.findByChatbox_ProjectIdAndMediaIdNotNull(projectId, pageable);

        List<MessageResponse> messageResponses = messages.stream()
                .map(this::fetchFromClients)
                .toList();

        return new PageImpl<>(messageResponses, pageable, totalElements);
    }

    @Override
    public MessageSourceResponse getSourceMessage(Long projectId, Long messageId, Pageable pageable) {
        Pageable ascendingSenttime = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("sentTime").ascending());

        Page<Message> olderMessages = messageRepository.findByChatbox_ProjectIdAndSentTimeLessThan(projectId, messageRepository.findById(messageId).get().getSentTime(), pageable);
        Page<Message> newerMessages = messageRepository.findByChatbox_ProjectIdAndSentTimeGreaterThan(projectId, messageRepository.findById(messageId).get().getSentTime(), ascendingSenttime);

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

