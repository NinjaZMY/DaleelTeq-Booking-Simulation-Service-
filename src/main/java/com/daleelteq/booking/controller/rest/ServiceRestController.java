package com.daleelteq.booking.controller.rest;

import com.daleelteq.booking.dto.ApiResponse;
import com.daleelteq.booking.dto.ServiceDto;
import com.daleelteq.booking.dto.IdRequestDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.service.ServiceService;
import com.daleelteq.booking.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/services")
@RequiredArgsConstructor
public class ServiceRestController {

    private static final Logger logger = LoggerFactory.getLogger(ServiceRestController.class);
    private final ServiceService serviceService;
    private final ServiceRepository serviceRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ServiceDto>>> getAllServices() {
        logger.info("GET /api/services - Fetch all services");
        List<ServiceDto> services = serviceService.getAllServices();
        return ResponseEntity.ok(ApiResponse.success(services, "Services retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceDto>> getServiceById(@PathVariable Long id) {
        logger.info("GET /api/services/{} - Fetch service by id", id);
        try {
            ServiceDto service = serviceService.getServiceById(id);
            return ResponseEntity.ok(ApiResponse.success(service, "Service retrieved successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = serviceRepository.findAll().stream()
                    .map(s -> s.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Service not found", availableIds));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ServiceDto>> createService(@RequestBody ServiceDto dto) {
        logger.info("POST /api/services - Create new service: {}", dto.getLib());
        try {
            ServiceDto created = serviceService.createService(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created, "Service created successfully"));
        } catch (Exception e) {
            logger.error("Error creating service", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create service", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ServiceDto>> updateServiceById(@PathVariable Long id, @RequestBody ServiceDto dto) {
        logger.info("PUT /api/services/{} - Update service", id);
        try {
            ServiceDto updated = serviceService.updateService(id, dto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Service updated successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = serviceRepository.findAll().stream()
                    .map(s -> s.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Service not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating service", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update service", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ServiceDto>> updateServiceByBody(@RequestBody IdRequestDto dto) {
        logger.info("PUT /api/services (JSON body) - Update service with id: {}", dto.getId());
        try {
            ServiceDto serviceDto = ServiceDto.builder()
                    .lib(dto.getLib())
                    .timeValue(dto.getTimeValue())
                    .build();
            ServiceDto updated = serviceService.updateService(dto.getId(), serviceDto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Service updated successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = serviceRepository.findAll().stream()
                    .map(s -> s.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Service not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating service", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update service", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteServiceById(@PathVariable Long id) {
        logger.info("DELETE /api/services/{} - Delete service", id);
        try {
            serviceService.deleteService(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = serviceRepository.findAll().stream()
                    .map(s -> s.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Service not found", availableIds));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteServiceByBody(@RequestBody IdRequestDto dto) {
        logger.info("DELETE /api/services (JSON body) - Delete service with id: {}", dto.getId());
        try {
            serviceService.deleteService(dto.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = serviceRepository.findAll().stream()
                    .map(s -> s.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Service not found", availableIds));
        }
    }
}

