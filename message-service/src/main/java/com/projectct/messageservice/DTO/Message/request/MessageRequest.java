package com.projectct.messageservice.DTO.Message.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageRequest {
     String fakeId;
     Long senderId;
     Long projectId;
     String content;
     Long mediaId;
     String authToken;
}
