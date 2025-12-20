package com.daleelteq.booking.controller.rest;

import com.daleelteq.booking.dto.ApiResponse;
import com.daleelteq.booking.dto.NotificationDto;
import com.daleelteq.booking.dto.IdRequestDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.service.NotificationService;
import com.daleelteq.booking.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationRestController {

    private static final Logger logger = LoggerFactory.getLogger(NotificationRestController.class);
    private final NotificationService notificationService;
    private final NotificationRepository notificationRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationDto>>> getAllNotifications() {
        logger.info("GET /api/notifications - Fetch all notifications");
        List<NotificationDto> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(ApiResponse.success(notifications, "Notifications retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationDto>> getNotificationById(@PathVariable Long id) {
        logger.info("GET /api/notifications/{} - Fetch notification by id", id);
        try {
            NotificationDto notification = notificationService.getNotificationById(id);
            return ResponseEntity.ok(ApiResponse.success(notification, "Notification retrieved successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = notificationRepository.findAll().stream()
                    .map(n -> n.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Notification not found", availableIds));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationDto>> createNotification(@RequestBody NotificationDto dto) {
        logger.info("POST /api/notifications - Create new notification for rendezvous id: {}", dto.getIdR());
        try {
            NotificationDto created = notificationService.createNotification(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created, "Notification created successfully"));
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Related entity not found"));
        } catch (Exception e) {
            logger.error("Error creating notification", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create notification", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotificationById(@PathVariable Long id) {
        logger.info("DELETE /api/notifications/{} - Delete notification", id);
        try {
            notificationService.deleteNotification(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = notificationRepository.findAll().stream()
                    .map(n -> n.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Notification not found", availableIds));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteNotificationByBody(@RequestBody IdRequestDto dto) {
        logger.info("DELETE /api/notifications (JSON body) - Delete notification with id: {}", dto.getId());
        try {
            notificationService.deleteNotification(dto.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = notificationRepository.findAll().stream()
                    .map(n -> n.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Notification not found", availableIds));
        }
    }
}

