package com.projectct.messageservice.DTO.Message.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypingMessageRequest {
    String username;
    String projectId;
    String authToken;
    Boolean isStop;
}
