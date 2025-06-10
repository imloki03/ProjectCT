package com.projectct.notificationservice.controller;

import com.projectct.notificationservice.DTO.Notification.response.NotificationResponse;
import com.projectct.notificationservice.DTO.RespondData;
import com.projectct.notificationservice.service.NotificationQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("notificationQs")
@RequiredArgsConstructor
public class NotificationQueueController {
    final NotificationQueueService notificationQueueService;

    @GetMapping("/{userId}/unread")
    public ResponseEntity<?> getAllUnReadNotificationOfUser(@PathVariable Long userId, Pageable pageable) {
        Page<NotificationResponse> responsePage = notificationQueueService.getAllUnReadNotificationOfUser(userId, pageable);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(responsePage)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }

    @GetMapping("/{userId}/read")
    public ResponseEntity<?> getAllReadNotificationOfUser(@PathVariable Long userId, Pageable pageable) {
        Page<NotificationResponse> responsePage = notificationQueueService.getAllReadNotificationOfUser(userId, pageable);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(responsePage)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
