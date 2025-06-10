package com.projectct.messageservice.DTO.Message.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PinMessageRequest {
    Long pinMessageId;
    Long projectId;
    String authToken;
    Long taskId;
}
