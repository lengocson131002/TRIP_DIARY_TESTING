package com.packandgo.tripdiary.service;

import com.packandgo.tripdiary.model.Notification;
import com.packandgo.tripdiary.model.User;
import org.springframework.data.domain.Page;

public interface NotificationService {
    Page<Notification> getNotification(User user, int page, int size);
    Notification markAsRead(User user, long notificationId);
    Notification unRead(User user, long notificationId);
    void readAll(User user);

    Notification saveNotification(Notification notification);

}