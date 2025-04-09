package com.projectct.messageservice.DTO.Message.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StoreMediaMessageResponse {
    Long mediaMessageId;
    Boolean success;
}
