package com.projectct.notificationservice.repository;

import com.projectct.notificationservice.model.NotificationQueue;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationQueueRepository extends JpaRepository<NotificationQueue, Long> {
}