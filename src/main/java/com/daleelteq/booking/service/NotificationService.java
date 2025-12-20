package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.Notification;
import com.daleelteq.booking.domain.RendezVous;
import com.daleelteq.booking.dto.NotificationDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.repository.NotificationRepository;
import com.daleelteq.booking.repository.RendezVousRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);
    private final NotificationRepository notificationRepository;
    private final RendezVousRepository rendezVousRepository;

    @Transactional(readOnly = true)
    public List<NotificationDto> getAllNotifications() {
        logger.info("Fetching all notifications");
        return notificationRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public NotificationDto getNotificationById(Long id) {
        logger.info("Fetching notification with id: {}", id);
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Notification", id));
        return convertToDto(notification);
    }

    @Transactional(readOnly = true)
    public List<NotificationDto> getNotificationsByRendezVousId(Long rendezVousId) {
        logger.info("Fetching notifications for rendezvous id: {}", rendezVousId);
        return notificationRepository.findByRendezVousId(rendezVousId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public NotificationDto createNotification(NotificationDto dto) {
        logger.info("Creating notification for rendezvous id: {}", dto.getIdR());
        
        RendezVous rendezVous = rendezVousRepository.findById(dto.getIdR())
                .orElseThrow(() -> new EntityNotFoundException("RendezVous", dto.getIdR()));
        
        Notification notification = Notification.builder()
                .rendezVous(rendezVous)
                .type(dto.getType())
                .value(dto.getValue())
                .build();
        
        Notification saved = notificationRepository.save(notification);
        logger.info("Notification created with id: {}", saved.getId());
        return convertToDto(saved);
    }

    @Transactional
    public void deleteNotification(Long id) {
        logger.info("Deleting notification with id: {}", id);
        
        if (!notificationRepository.existsById(id)) {
            throw new EntityNotFoundException("Notification", id);
        }
        
        notificationRepository.deleteById(id);
        logger.info("Notification deleted with id: {}", id);
    }

    private NotificationDto convertToDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .idR(notification.getRendezVous().getId())
                .type(notification.getType())
                .value(notification.getValue())
                .createdAt(notification.getCreatedAt())
                .build();
    }
}

