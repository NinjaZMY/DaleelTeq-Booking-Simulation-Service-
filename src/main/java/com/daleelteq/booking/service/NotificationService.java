package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.Notification;
import com.daleelteq.booking.dto.NotificationDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;

    /**
     * Get all notifications
     */
    public List<NotificationDto> getAllNotifications() {
        log.debug("Fetching all notifications");
        return notificationRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get notification by ID
     */
    public NotificationDto getNotificationById(Long id) {
        log.debug("Fetching notification with id: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableNotificationIds();
                    log.warn("Notification not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("Notification with id %d not found. Available notification ids: %s", id, availableIds)
                    );
                });
        return toDto(notification);
    }

    /**
     * Get notifications by rendez-vous ID
     */
    public List<NotificationDto> getNotificationsByRendezVousId(Long idR) {
        log.debug("Fetching notifications for rendez-vous id: {}", idR);
        return notificationRepository.findByIdR(idR).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get notifications by type (booked or cancelled)
     */
    public List<NotificationDto> getNotificationsByType(String type) {
        log.debug("Fetching notifications by type: {}", type);
        return notificationRepository.findByType(type).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Create notification (for testing purposes)
     */
    public NotificationDto createNotification(NotificationDto dto) {
        log.info("Creating notification for rendez-vous id: {}", dto.getIdR());

        Notification notification = Notification.builder()
                .idR(dto.getIdR())
                .type(dto.getType())
                .value(dto.getValue())
                .x2(dto.getX2() != null ? dto.getX2() : false)
                .timeValue(dto.getTimeValue() != null ? dto.getTimeValue() : 0)
                .build();

        Notification saved = notificationRepository.save(notification);
        log.info("Notification created successfully with id: {}", saved.getId());
        return toDto(saved);
    }

    /**
     * Delete notification
     */
    public void deleteNotification(Long id) {
        log.info("Deleting notification with id: {}", id);

        if (!notificationRepository.existsById(id)) {
            String availableIds = getAvailableNotificationIds();
            log.warn("Notification not found with id: {}. Available ids: {}", id, availableIds);
            throw new EntityNotFoundException(
                    String.format("Notification with id %d not found. Available notification ids: %s", id, availableIds)
            );
        }

        notificationRepository.deleteById(id);
        log.info("Notification deleted successfully with id: {}", id);
    }

    /**
     * Delete all notifications
     */
    public void deleteAllNotifications() {
        log.warn("Deleting all notifications");
        notificationRepository.deleteAll();
        log.info("All notifications deleted");
    }

    /**
     * Helper method to get available notification IDs for error messages
     */
    private String getAvailableNotificationIds() {
        return notificationRepository.findAll().stream()
                .map(n -> String.valueOf(n.getId()))
                .collect(Collectors.joining(",", "[", "]"));
    }

    /**
     * Convert entity to DTO
     */
    private NotificationDto toDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .idR(notification.getIdR())
                .type(notification.getType())
                .value(notification.getValue())
                .x2(notification.getX2())
                .timeValue(notification.getTimeValue())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}
