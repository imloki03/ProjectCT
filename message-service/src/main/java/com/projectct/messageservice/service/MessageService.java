package com.projectct.messageservice.service;

import com.projectct.messageservice.DTO.Message.request.MessageRequest;
import com.projectct.messageservice.DTO.Message.request.PinMessageRequest;
import com.projectct.messageservice.DTO.Message.request.ReadMessageRequest;

public interface MessageService {
    void sendMessage(MessageRequest request);

    void pinMessage(PinMessageRequest request);

    void readMessage(ReadMessageRequest request);

    void typingMessage(MessageRequest request);
}
