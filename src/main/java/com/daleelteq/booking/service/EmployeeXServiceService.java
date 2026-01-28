package com.daleelteq.booking.service;

import com.daleelteq.booking.config.TimeWindowConfig;
import com.daleelteq.booking.domain.EmployeeXService;
import com.daleelteq.booking.domain.Service;
import com.daleelteq.booking.dto.EmployeeXServiceDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.exception.ValidationException;
import com.daleelteq.booking.repository.EmployeeXServiceRepository;
import com.daleelteq.booking.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class EmployeeXServiceService {

    private final EmployeeXServiceRepository esRepository;
    private final ServiceRepository serviceRepository;
    private final TimeWindowConfig timeWindowConfig;

    /**
     * Get all ES timeslots
     */
    public List<EmployeeXServiceDto> getAllES() {
        log.debug("Fetching all ES timeslots");
        return esRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get ES by ID
     */
    public EmployeeXServiceDto getESById(Long id) {
        log.debug("Fetching ES with id: {}", id);
        EmployeeXService es = esRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableESIds();
                    log.warn("ES not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("ES with id %d not found. Available ES ids: %s", id, availableIds)
                    );
                });
        return toDto(es);
    }

    /**
     * Create new ES timeslot
     * Validates date, time window, and computes end time based on service duration and x_2 flag
     */
    public EmployeeXServiceDto createES(EmployeeXServiceDto dto) {
        log.info("Creating new ES timeslot for employee: {}, service: {}", dto.getIdE(), dto.getIdS());

        // Fetch service to get base time value
        Service service = serviceRepository.findById(dto.getIdS())
                .orElseThrow(() -> {
                    String availableIds = getAvailableServiceIds();
                    log.warn("Service not found with id: {}. Available ids: {}", dto.getIdS(), availableIds);
                    return new EntityNotFoundException(
                            String.format("Service with id %d not found. Available service ids: %s", dto.getIdS(), availableIds)
                    );
                });

        // Validate date
        if (dto.getDate() == null) {
            throw new ValidationException("Invalid date: date is required. Expected format: YYYY-MM-DD");
        }

        // Validate start time is within allowed window
        if (dto.getStart() == null) {
            throw new ValidationException(
                    String.format("Invalid time: start time is required. Valid time window: %s to %s",
                            timeWindowConfig.getStart(), timeWindowConfig.getEnd())
            );
        }

        // Calculate time value (base or doubled)
        int baseTimeValue = service.getTimeValue();
        int computedTimeValue = (dto.getX2() != null && dto.getX2()) ? baseTimeValue * 2 : baseTimeValue;

        // Validate start time is within window and compute end time
        LocalTime computedEnd = dto.getStart().plusMinutes(computedTimeValue);

        if (dto.getStart().isBefore(timeWindowConfig.getStart()) || dto.getStart().isAfter(timeWindowConfig.getEnd())) {
            throw new ValidationException(
                    String.format("Invalid start time: %s is outside allowed window %s to %s. " +
                                    "For a %d-minute slot, start must be between %s and %s.",
                            dto.getStart(), timeWindowConfig.getStart(), timeWindowConfig.getEnd(),
                            computedTimeValue,
                            timeWindowConfig.getStart(),
                            timeWindowConfig.getEnd().minusMinutes(computedTimeValue))
            );
        }

        if (computedEnd.isAfter(timeWindowConfig.getEnd())) {
            throw new ValidationException(
                    String.format("Invalid time: end %s exceeds allowed window %s–%s for timeValue %d. " +
                                    "For a %d-minute slot, start must be between %s and %s.",
                            computedEnd, timeWindowConfig.getStart(), timeWindowConfig.getEnd(),
                            computedTimeValue, computedTimeValue,
                            timeWindowConfig.getStart(),
                            timeWindowConfig.getEnd().minusMinutes(computedTimeValue))
            );
        }

        // Create ES
        EmployeeXService es = EmployeeXService.builder()
                .idE(dto.getIdE())
                .idS(dto.getIdS())
                .x2(dto.getX2() != null ? dto.getX2() : false)
                .date(dto.getDate())
                .startTime(dto.getStart())
                .endTime(computedEnd)
                .timeValue(computedTimeValue)
                .status("free")
                .build();

        EmployeeXService saved = esRepository.save(es);
        log.info("ES created successfully with id: {}. Start: {}, End: {}, TimeValue: {}, X2: {}",
                saved.getId(), saved.getStart(), saved.getEnd(), saved.getTimeValue(), saved.getX2());

        return toDto(saved);
    }

    /**
     * Update ES timeslot
     */
    public EmployeeXServiceDto updateES(Long id, EmployeeXServiceDto dto) {
        log.info("Updating ES with id: {}", id);

        EmployeeXService es = esRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableESIds();
                    log.warn("ES not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("ES with id %d not found. Available ES ids: %s", id, availableIds)
                    );
                });

        // If start time or x2 changes, recompute end time
        if (dto.getStart() != null || dto.getX2() != null) {
            LocalTime newStart = dto.getStart() != null ? dto.getStart() : es.getStart();
            Integer timeValue = es.getTimeValue();

            // Validate new start time
            if (newStart.isBefore(timeWindowConfig.getStart()) || newStart.isAfter(timeWindowConfig.getEnd())) {
                throw new ValidationException(
                        String.format("Invalid start time: %s is outside allowed window %s to %s.",
                                newStart, timeWindowConfig.getStart(), timeWindowConfig.getEnd())
                );
            }

            LocalTime newEnd = newStart.plusMinutes(timeValue);
            if (newEnd.isAfter(timeWindowConfig.getEnd())) {
                throw new ValidationException(
                        String.format("Invalid time: end %s exceeds allowed window %s–%s for timeValue %d.",
                                newEnd, timeWindowConfig.getStart(), timeWindowConfig.getEnd(), timeValue)
                );
            }

            es.setStart(newStart);
            es.setEnd(newEnd);
        }

        if (dto.getDate() != null) {
            es.setDate(dto.getDate());
        }
        if (dto.getStatus() != null) {
            es.setStatus(dto.getStatus());
        }

        EmployeeXService updated = esRepository.save(es);
        log.info("ES updated successfully with id: {}", updated.getId());
        return toDto(updated);
    }

    /**
     * Delete ES timeslot
     */
    public void deleteES(Long id) {
        log.info("Deleting ES with id: {}", id);

        if (!esRepository.existsById(id)) {
            String availableIds = getAvailableESIds();
            log.warn("ES not found with id: {}. Available ids: {}", id, availableIds);
            throw new EntityNotFoundException(
                    String.format("ES with id %d not found. Available ES ids: %s", id, availableIds)
            );
        }

        esRepository.deleteById(id);
        log.info("ES deleted successfully with id: {}", id);
    }

    /**
     * Delete all ES timeslots
     */
    public void deleteAllES() {
        log.warn("Deleting all ES timeslots");
        esRepository.deleteAll();
        log.info("All ES deleted");
    }

    /**
     * Get free ES timeslots for a given date
     */
    public List<EmployeeXServiceDto> getFreeESByDate(LocalDate date) {
        log.debug("Fetching free ES for date: {}", date);
        return esRepository.findByDate(date).stream()
                .filter(es -> "free".equals(es.getStatus()))
                .map(this::toDto)
                .collect(Collectors.toList());
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
     * Helper method to get available Service IDs for error messages
     */
    private String getAvailableServiceIds() {
        return serviceRepository.findAll().stream()
                .map(s -> String.valueOf(s.getId()))
                .collect(Collectors.joining(",", "[", "]"));
    }

    /**
     * Convert entity to DTO
     */
    private EmployeeXServiceDto toDto(EmployeeXService es) {
        return EmployeeXServiceDto.builder()
                .id(es.getId())
                .idE(es.getIdE())
                .idS(es.getIdS())
                .x2(es.getX2())
                .date(es.getDate())
                .start(es.getStart())
                .end(es.getEnd())
                .timeValue(es.getTimeValue())
                .status(es.getStatus())
                .createdAt(es.getCreatedAt())
                .updatedAt(es.getUpdatedAt())
                .build();
    }
}
