package com.projectct.messageservice.DTO.Message.response;

import com.projectct.messageservice.DTO.Media.response.MediaResponse;
import com.projectct.messageservice.DTO.User.response.UserResponse;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
    Long id;
    UserResponse user;
    String content;
    String project;
    LocalDateTime sentTime;
    List<String> readerList;
    Boolean isPinned;
    MediaResponse media;
}
