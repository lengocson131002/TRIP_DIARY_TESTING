package com.packandgo.tripdiary.repository;

import com.packandgo.tripdiary.model.Notification;
import com.packandgo.tripdiary.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @Query("SELECT n FROM Notification n WHERE n.user.id = ?1 ORDER BY n.createdAt DESC")
    Page<Notification> getNotificationsForUser(Long userId, Pageable pageable);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = TRUE WHERE n.user.id = ?1")
    void readAll(long userId);

    @Query("FROM Notification n WHERE n.id = ?2 AND n.user.id = ?1")
    Optional<Notification> existForUser(long userId, long notificationId);
}
