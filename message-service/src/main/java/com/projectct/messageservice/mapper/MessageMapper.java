package com.projectct.messageservice.mapper;

import com.projectct.messageservice.DTO.Message.request.MessageRequest;
import com.projectct.messageservice.DTO.Message.response.MessageResponse;
import com.projectct.messageservice.model.Message;
import com.projectct.messageservice.model.MessageReader;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MessageMapper {
    Message toMessage(MessageRequest messageRequest);

    @Mapping(source = "pinned", target = "isPinned")
    @Mapping(target = "readerList", expression = "java(mapReaderUsernames(message.getReaderList()))")
    MessageResponse toMessageResponse(Message message);
    List<MessageResponse> toMessageResponseList(List<Message> messages);

    default List<String> mapReaderUsernames(List<MessageReader> readers) {
        if (readers == null) {
            return Collections.emptyList();
        }
        return readers.stream()
                .map(MessageReader::getReaderUsername)
                .toList();
    }
}

