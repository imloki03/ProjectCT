package com.projectct.notificationservice.controller;

import com.projectct.notificationservice.DTO.Notification.response.NotificationResponse;
import com.projectct.notificationservice.DTO.RespondData;
import com.projectct.notificationservice.service.NotificationQueueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("{userId}")
    public ResponseEntity<?> getAllNotificationOfUser(@PathVariable Long userId) {
        List<NotificationResponse> responseList = notificationQueueService.getAllNotificationOfUser(userId);
        var respondData = RespondData
                .builder()
                .status(HttpStatus.OK.value())
                .data(responseList)
                .build();
        return new ResponseEntity<>(respondData, HttpStatus.OK);
    }
}
