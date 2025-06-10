package com.projectct.messageservice.DTO.Message.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadMessageRequest {
    Long projectId;
    String username;
    String authToken;
    Long lastSeenMessageId;
    Long taskId;
}
