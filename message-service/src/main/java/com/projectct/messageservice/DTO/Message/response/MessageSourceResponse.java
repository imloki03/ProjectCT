package com.projectct.messageservice.DTO.Message.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageSourceResponse {
    Page<MessageResponse> messages;
    boolean hasMoreOlder;
    boolean hasMoreNewer;
}
