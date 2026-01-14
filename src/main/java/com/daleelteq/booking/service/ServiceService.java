package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.Service;
import com.daleelteq.booking.dto.ServiceDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class ServiceService {

    private final ServiceRepository serviceRepository;

    /**
     * Get all services
     */
    public List<ServiceDto> getAllServices() {
        log.debug("Fetching all services");
        return serviceRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get service by ID
     */
    public ServiceDto getServiceById(Long id) {
        log.debug("Fetching service with id: {}", id);
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableServiceIds();
                    log.warn("Service not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("Service with id %d not found. Available service ids: %s", id, availableIds)
                    );
                });
        return toDto(service);
    }

    /**
     * Create new service
     */
    public ServiceDto createService(ServiceDto dto) {
        log.info("Creating new service: {}", dto.getLib());

        // Validate time value
        if (dto.getTimeValue() == null || !isValidTimeValue(dto.getTimeValue())) {
            throw new IllegalArgumentException(
                    "Invalid timeValue. Allowed values: [15, 20, 25, 30]. Provided: " + dto.getTimeValue()
            );
        }

        Service service = Service.builder()
                .lib(dto.getLib())
                .timeValue(dto.getTimeValue())
                .build();

        Service saved = serviceRepository.save(service);
        log.info("Service created successfully with id: {}", saved.getId());
        return toDto(saved);
    }

    /**
     * Update service
     */
    public ServiceDto updateService(Long id, ServiceDto dto) {
        log.info("Updating service with id: {}", id);

        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableServiceIds();
                    log.warn("Service not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("Service with id %d not found. Available service ids: %s", id, availableIds)
                    );
                });

        if (dto.getLib() != null) {
            service.setLib(dto.getLib());
        }
        if (dto.getTimeValue() != null) {
            if (!isValidTimeValue(dto.getTimeValue())) {
                throw new IllegalArgumentException(
                        "Invalid timeValue. Allowed values: [15, 20, 25, 30]. Provided: " + dto.getTimeValue()
                );
            }
            service.setTimeValue(dto.getTimeValue());
        }

        Service updated = serviceRepository.save(service);
        log.info("Service updated successfully with id: {}", updated.getId());
        return toDto(updated);
    }

    /**
     * Delete service
     */
    public void deleteService(Long id) {
        log.info("Deleting service with id: {}", id);

        if (!serviceRepository.existsById(id)) {
            String availableIds = getAvailableServiceIds();
            log.warn("Service not found with id: {}. Available ids: {}", id, availableIds);
            throw new EntityNotFoundException(
                    String.format("Service with id %d not found. Available service ids: %s", id, availableIds)
            );
        }

        serviceRepository.deleteById(id);
        log.info("Service deleted successfully with id: {}", id);
    }

    /**
     * Delete all services
     */
    public void deleteAllServices() {
        log.warn("Deleting all services");
        serviceRepository.deleteAll();
        log.info("All services deleted");
    }

    /**
     * Helper method to validate time value
     */
    private boolean isValidTimeValue(Integer timeValue) {
        return timeValue != null && (timeValue == 15 || timeValue == 20 || timeValue == 25 || timeValue == 30);
    }

    /**
     * Helper method to get available service IDs for error messages
     */
    private String getAvailableServiceIds() {
        return serviceRepository.findAll().stream()
                .map(s -> String.valueOf(s.getId()))
                .collect(Collectors.joining(",", "[", "]"));
    }

    /**
     * Convert entity to DTO
     */
    private ServiceDto toDto(Service service) {
        return ServiceDto.builder()
                .id(service.getId())
                .lib(service.getLib())
                .timeValue(service.getTimeValue())
                .createdAt(service.getCreatedAt())
                .build();
    }
}
