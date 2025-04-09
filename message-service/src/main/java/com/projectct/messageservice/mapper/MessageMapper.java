package com.projectct.messageservice.mapper;

import com.projectct.messageservice.DTO.Message.request.MessageRequest;
import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import com.projectct.messageservice.model.Message;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toMessage(MessageRequest messageRequest);
    @Mapping(source = "pinned", target = "isPinned")
    MessageResponse toMessageResponse(Message message);
    List<MessageResponse> toMessageResponseList(List<Message> messages);
}
