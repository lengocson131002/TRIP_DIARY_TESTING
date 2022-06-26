package com.packandgo.tripdiary.service.impl;

import com.packandgo.tripdiary.exception.NotificationNotFoundException;
import com.packandgo.tripdiary.model.Notification;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.repository.NotificationRepository;
import com.packandgo.tripdiary.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Page<Notification> getNotification(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Notification> notifications =notificationRepository.getNotificationsForUser(user.getId(), pageable);
        return notifications;
    }

    @Override
    @Transactional
    public Notification markAsRead(User user, long notificationId) {
        if(!notificationRepository.existsById(notificationId)) {
            throw  new NotificationNotFoundException("Notification doesn't exist");
        }
        Notification notification = notificationRepository
                .existForUser(user.getId(), notificationId)
                .orElseThrow(() -> new IllegalArgumentException("User have no permission for this notification"));

        notification.setRead(true);
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public Notification unRead(User user, long notificationId) {
        if(!notificationRepository.existsById(notificationId)) {
            throw  new NotificationNotFoundException("Notification doesn't exist");
        }
        Notification notification = notificationRepository
                .existForUser(user.getId(), notificationId)
                .orElseThrow(() -> new IllegalArgumentException("User have no permission for this notification"));
        notification.setRead(false);
        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void readAll(User user) {
        notificationRepository.readAll(user.getId());
    }

    @Override
    public Notification saveNotification(Notification notification) {
        if(notification.getTrip() == null) {
            return null;
        }
        return notificationRepository.save(notification);
    }
}
