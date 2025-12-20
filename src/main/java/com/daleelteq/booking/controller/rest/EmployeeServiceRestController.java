package com.daleelteq.booking.controller.rest;

import com.daleelteq.booking.dto.ApiResponse;
import com.daleelteq.booking.dto.EmployeeServiceDto;
import com.daleelteq.booking.dto.IdRequestDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.exception.ValidationException;
import com.daleelteq.booking.service.EmployeeServiceService;
import com.daleelteq.booking.repository.EmployeeServiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/es")
@RequiredArgsConstructor
public class EmployeeServiceRestController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceRestController.class);
    private final EmployeeServiceService employeeServiceService;
    private final EmployeeServiceRepository employeeServiceRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeServiceDto>>> getAllEmployeeServices() {
        logger.info("GET /api/es - Fetch all employee services");
        List<EmployeeServiceDto> services = employeeServiceService.getAllEmployeeServices();
        return ResponseEntity.ok(ApiResponse.success(services, "Employee services retrieved successfully"));
    }

    @GetMapping("/free")
    public ResponseEntity<ApiResponse<List<EmployeeServiceDto>>> getFreeEmployeeServices() {
        logger.info("GET /api/es/free - Fetch free employee services");
        List<EmployeeServiceDto> services = employeeServiceService.getFreeEmployeeServices();
        return ResponseEntity.ok(ApiResponse.success(services, "Free employee services retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeServiceDto>> getEmployeeServiceById(@PathVariable Long id) {
        logger.info("GET /api/es/{} - Fetch employee service by id", id);
        try {
            EmployeeServiceDto service = employeeServiceService.getEmployeeServiceById(id);
            return ResponseEntity.ok(ApiResponse.success(service, "Employee service retrieved successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeServiceRepository.findAll().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "EmployeeService not found", availableIds));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeServiceDto>> createEmployeeService(@RequestBody EmployeeServiceDto dto) {
        logger.info("POST /api/es - Create new employee service");
        try {
            EmployeeServiceDto created = employeeServiceService.createEmployeeService(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created, "Employee service created successfully"));
        } catch (ValidationException e) {
            logger.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Validation failed", e.getMessage()));
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Related entity not found"));
        } catch (Exception e) {
            logger.error("Error creating employee service", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create employee service", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeServiceDto>> updateEmployeeServiceById(@PathVariable Long id, @RequestBody EmployeeServiceDto dto) {
        logger.info("PUT /api/es/{} - Update employee service", id);
        try {
            EmployeeServiceDto updated = employeeServiceService.updateEmployeeService(id, dto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Employee service updated successfully"));
        } catch (ValidationException e) {
            logger.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Validation failed", e.getMessage()));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeServiceRepository.findAll().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "EmployeeService not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating employee service", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update employee service", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<EmployeeServiceDto>> updateEmployeeServiceByBody(@RequestBody IdRequestDto dto) {
        logger.info("PUT /api/es (JSON body) - Update employee service with id: {}", dto.getId());
        try {
            EmployeeServiceDto serviceDto = EmployeeServiceDto.builder()
                    .idE(dto.getIdE())
                    .idS(dto.getIdS())
                    .x2(dto.getX2())
                    .start(dto.getStart() != null ? java.time.LocalTime.parse(dto.getStart()) : null)
                    .end(dto.getEnd() != null ? java.time.LocalTime.parse(dto.getEnd()) : null)
                    .date(dto.getDate() != null ? java.time.LocalDate.parse(dto.getDate()) : null)
                    .status(dto.getStatus())
                    .build();
            EmployeeServiceDto updated = employeeServiceService.updateEmployeeService(dto.getId(), serviceDto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Employee service updated successfully"));
        } catch (ValidationException e) {
            logger.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Validation failed", e.getMessage()));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeServiceRepository.findAll().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "EmployeeService not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating employee service", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update employee service", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployeeServiceById(@PathVariable Long id) {
        logger.info("DELETE /api/es/{} - Delete employee service", id);
        try {
            employeeServiceService.deleteEmployeeService(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeServiceRepository.findAll().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "EmployeeService not found", availableIds));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteEmployeeServiceByBody(@RequestBody IdRequestDto dto) {
        logger.info("DELETE /api/es (JSON body) - Delete employee service with id: {}", dto.getId());
        try {
            employeeServiceService.deleteEmployeeService(dto.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeServiceRepository.findAll().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "EmployeeService not found", availableIds));
        }
    }
}

