package com.projectct.notificationservice.service;

import com.projectct.notificationservice.DTO.Notification.response.NotificationResponse;
import com.projectct.notificationservice.mapper.NotiMapper;
import com.projectct.notificationservice.repository.NotificationQueueRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationQueueServiceImpl implements NotificationQueueService {
    final NotificationQueueRepository notificationQueueRepository;
    final NotiMapper notificationMapper;

    @Override
    public List<NotificationResponse> getAllNotificationOfUser(Long userId) {
        return notificationMapper.toResponseList(notificationQueueRepository.findByUserId(userId).getNotificationList());
    }
}
