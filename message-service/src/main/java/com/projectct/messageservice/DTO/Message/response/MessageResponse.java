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
    UserResponse sender;
    String content;
    String project;
    LocalDateTime sentTime;
    LocalDateTime pinTime;
    List<String> readerList;
    Boolean isPinned;
    Boolean inStorage;
    MediaResponse media;
    String fakeId;
}
