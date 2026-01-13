package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.EmployeeXService;
import com.daleelteq.booking.domain.Notification;
import com.daleelteq.booking.domain.RendezVous;
import com.daleelteq.booking.dto.RendezVousDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.exception.ValidationException;
import com.daleelteq.booking.repository.EmployeeXServiceRepository;
import com.daleelteq.booking.repository.NotificationRepository;
import com.daleelteq.booking.repository.RendezVousRepository;
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
public class RendezVousService {

    private final RendezVousRepository rendezVousRepository;
    private final EmployeeXServiceRepository esRepository;
    private final NotificationRepository notificationRepository;

    /**
     * Get all rendez-vous
     */
    public List<RendezVousDto> getAllRendezVous() {
        log.debug("Fetching all rendez-vous");
        return rendezVousRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get rendez-vous by ID
     */
    public RendezVousDto getRendezVousById(Long id) {
        log.debug("Fetching rendez-vous with id: {}", id);
        RendezVous rv = rendezVousRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableRendezVousIds();
                    log.warn("Rendez-vous not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("Rendez-vous with id %d not found. Available rendez-vous ids: %s", id, availableIds)
                    );
                });
        return toDto(rv);
    }

    /**
     * Book a timeslot (create rendez-vous)
     * Automatically creates a Notification with type='booked'
     */
    @Transactional
    public RendezVousDto bookRendezVous(Long esId, Long clientId) {
        log.info("Booking rendez-vous for ES id: {}, Client id: {}", esId, clientId);

        // Validate ES exists and is free
        EmployeeXService es = esRepository.findById(esId)
                .orElseThrow(() -> {
                    String availableIds = getAvailableESIds();
                    log.warn("ES not found with id: {}. Available ids: {}", esId, availableIds);
                    return new EntityNotFoundException(
                            String.format("ES with id %d not found. Available ES ids: %s", esId, availableIds)
                    );
                });

        if (!"free".equals(es.getStatus())) {
            List<Long> availableFreeESIds = esRepository.findByStatus("free").stream()
                    .map(EmployeeXService::getId)
                    .collect(Collectors.toList());
            log.warn("ES id: {} is not free. Available free ES ids: {}", esId, availableFreeESIds);
            throw new ValidationException(
                    String.format("Timeslot ES id %d is already taken. Available free ES ids: %s", esId, availableFreeESIds)
            );
        }

        // Create RendezVous
        RendezVous rv = RendezVous.builder()
                .idES(esId)
                .idC(clientId)
                .status("Active")
                .build();

        RendezVous saved = rendezVousRepository.save(rv);

        // Update ES status to 'taken'
        es.setStatus("taken");
        esRepository.save(es);

        // Create Notification for booking
        Notification notification = Notification.builder()
                .idR(saved.getId())
                .type("booked")
                .value("Active")
                .x2(es.getX2())
                .timeValue(es.getTimeValue())
                .build();
        notificationRepository.save(notification);

        log.info("Rendez-vous booked successfully with id: {}. ES id: {} marked as 'taken'", saved.getId(), esId);
        return toDto(saved);
    }

    /**
     * Cancel a rendez-vous
     * Updates ES status back to 'free' and creates a Notification with type='cancelled'
     */
    @Transactional
    public RendezVousDto cancelRendezVous(Long id, String cancelledBy) {
        log.info("Cancelling rendez-vous with id: {} by {}", id, cancelledBy);

        RendezVous rv = rendezVousRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableRendezVousIds();
                    log.warn("Rendez-vous not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("Rendez-vous with id %d not found. Available rendez-vous ids: %s", id, availableIds)
                    );
                });

        if (rv.getStatus().contains("Cancelled")) {
            log.warn("Rendez-vous id: {} is already cancelled", id);
            throw new ValidationException(
                    String.format("Rendez-vous id %d is already cancelled with status: %s", id, rv.getStatus())
            );
        }

        String newStatus = "Cancelled by " + cancelledBy;
        rv.setStatus(newStatus);
        RendezVous updated = rendezVousRepository.save(rv);

        // Set ES back to 'free'
        EmployeeXService es = esRepository.findById(rv.getIdES())
                .orElseThrow(() -> new EntityNotFoundException(
                        String.format("ES with id %d not found", rv.getIdES())
                ));
        es.setStatus("free");
        esRepository.save(es);

        // Create Notification for cancellation
        Notification notification = Notification.builder()
                .idR(updated.getId())
                .type("cancelled")
                .value(newStatus)
                .x2(es.getX2())
                .timeValue(es.getTimeValue())
                .build();
        notificationRepository.save(notification);

        log.info("Rendez-vous id: {} cancelled successfully. ES id: {} marked as 'free'. Status: {}", id, rv.getIdES(), newStatus);
        return toDto(updated);
    }

    /**
     * Delete rendez-vous
     */
    public void deleteRendezVous(Long id) {
        log.info("Deleting rendez-vous with id: {}", id);

        if (!rendezVousRepository.existsById(id)) {
            String availableIds = getAvailableRendezVousIds();
            log.warn("Rendez-vous not found with id: {}. Available ids: {}", id, availableIds);
            throw new EntityNotFoundException(
                    String.format("Rendez-vous with id %d not found. Available rendez-vous ids: %s", id, availableIds)
            );
        }

        rendezVousRepository.deleteById(id);
        log.info("Rendez-vous deleted successfully with id: {}", id);
    }

    /**
     * Delete all rendez-vous
     */
    public void deleteAllRendezVous() {
        log.warn("Deleting all rendez-vous");
        rendezVousRepository.deleteAll();
        log.info("All rendez-vous deleted");
    }

    /**
     * Helper method to get available Rendez-vous IDs for error messages
     */
    private String getAvailableRendezVousIds() {
        return rendezVousRepository.findAll().stream()
                .map(rv -> String.valueOf(rv.getId()))
                .collect(Collectors.joining(",", "[", "]"));
    }

    /**
     * Helper method to get available ES IDs for error messages
     */
    private String getAvailableESIds() {
        return esRepository.findAll().stream()
                .map(es -> String.valueOf(es.getId()))
                .collect(Collectors.joining(",", "[", "]"));
    }

    /**
     * Convert entity to DTO
     */
    private RendezVousDto toDto(RendezVous rv) {
        return RendezVousDto.builder()
                .id(rv.getId())
                .idES(rv.getIdES())
                .idC(rv.getIdC())
                .status(rv.getStatus())
                .createdAt(rv.getCreatedAt())
                .updatedAt(rv.getUpdatedAt())
                .build();
    }
}
