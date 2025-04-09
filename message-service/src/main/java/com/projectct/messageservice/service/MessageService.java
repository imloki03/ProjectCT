package com.projectct.messageservice.service;

import com.projectct.messageservice.DTO.Message.request.*;

public interface MessageService {
    void sendMessage(MessageRequest request);

    void pinMessage(PinMessageRequest request);

    void readMessage(ReadMessageRequest request);

    void typingMessage(TypingMessageRequest request);

    void storeMediaMessage(StoreMediaMessageRequest request);
}
