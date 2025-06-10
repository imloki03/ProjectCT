package com.projectct.messageservice.DTO.Message.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TypingMessageRequest {
    String username;
    Long projectId;
    String authToken;
    Long taskId;
    Boolean isStop;
}
