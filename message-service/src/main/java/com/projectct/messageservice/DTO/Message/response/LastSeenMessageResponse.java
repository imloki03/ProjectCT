package com.projectct.messageservice.DTO.Message.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.Map;

@Builder
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class LastSeenMessageResponse {
    Map<Long, List<String>> lastSeenMessageMap;
}
