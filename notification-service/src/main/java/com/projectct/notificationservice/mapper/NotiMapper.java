package com.projectct.notificationservice.mapper;

import com.projectct.notificationservice.DTO.Notification.response.NotificationResponse;
import com.projectct.notificationservice.model.Notification;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotiMapper {
    NotificationResponse toResponse(Notification notification);
    List<NotificationResponse> toResponseList(List<Notification> notifications);
}
