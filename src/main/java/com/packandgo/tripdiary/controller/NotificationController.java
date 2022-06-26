package com.packandgo.tripdiary.controller;

import com.packandgo.tripdiary.enums.NotificationType;
import com.packandgo.tripdiary.model.Notification;
import com.packandgo.tripdiary.model.User;
import com.packandgo.tripdiary.payload.response.MessageResponse;
import com.packandgo.tripdiary.payload.response.NotificationResponse;
import com.packandgo.tripdiary.payload.response.PagingResponse;
import com.packandgo.tripdiary.service.NotificationService;
import com.packandgo.tripdiary.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final UserService userService;
    private final NotificationService notificationService;

    public NotificationController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @GetMapping("")
    public ResponseEntity<?> getAllNotifications(
            @RequestParam(name = "page", required = false, defaultValue = "1") int page,
            @RequestParam(name = "size", required = false, defaultValue = "5") int size
    ) {
        page = page != 0 ? page : 1;
        size = size != 0 ? size : 5;

        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userService.findUserByUsername(userDetails.getUsername());
        Page<Notification> notifications = notificationService.getNotification(user, page, size);
        List<NotificationResponse> notificationResponses = notifications
                .getContent()
                .stream()
                .map(n -> {
                    NotificationResponse response = new NotificationResponse();
                    response.setId(n.getId());
                    response.setRead(n.isRead());
                    response.setCreatedAt(n.getCreatedAt());
                    response.setType(NotificationType.COMING_TRIP == n.getType() ? "comming_trip" : "invitation");
                    response.setTrip(n.getTrip().toResponse());
                    return response;
                }).collect(Collectors.toList());

        PagingResponse<NotificationResponse> response = new PagingResponse<>(page,
                size,
                notifications.getTotalPages(),
                notificationResponses
        );
        return ResponseEntity.ok(response);

    }

    @PostMapping("/{id}/read")
    public ResponseEntity<?> markAsRead(@PathVariable(name = "id", required = true) Long notificationId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userService.findUserByUsername(userDetails.getUsername());
        Notification notification = notificationService.markAsRead(user, notificationId);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/{id}/unread")
    public ResponseEntity<?> unRead(@PathVariable(name = "id", required = true) Long notificationId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userService.findUserByUsername(userDetails.getUsername());
        Notification notification = notificationService.unRead(user, notificationId);
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/read-all")
    public ResponseEntity<?> readAll() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User user = userService.findUserByUsername(userDetails.getUsername());
        notificationService.readAll(user);
        return ResponseEntity.ok(new MessageResponse("All notification are read"));
    }
}
