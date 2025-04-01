package com.projectct.messageservice.DTO.Message.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LastSeenMessageRequest {
    List<String> userNameList;
}
