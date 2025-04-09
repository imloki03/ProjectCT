package com.projectct.messageservice.DTO.Message.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreMediaMessageRequest {
    Long mediaMessageId;
    Long mediaId;
    Long projectId;
    String authToken;
}
