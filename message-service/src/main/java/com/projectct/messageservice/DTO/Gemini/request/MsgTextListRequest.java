package com.projectct.messageservice.DTO.Gemini.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MsgTextListRequest {
    List<String> messages;
}
