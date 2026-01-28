package com.daleelteq.booking.controller.rest;

import com.daleelteq.booking.dto.ApiResponse;
import com.daleelteq.booking.dto.EmployeeXServiceDto;
import com.daleelteq.booking.dto.IdRequestDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.exception.ValidationException;
import com.daleelteq.booking.service.EmployeeXServiceService;
import com.daleelteq.booking.repository.EmployeeXServiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/es")
@RequiredArgsConstructor
public class EmployeeXServiceRestController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeXServiceRestController.class);
    private final EmployeeXServiceService employeeXServiceService;
    private final EmployeeXServiceRepository employeeXServiceRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeXServiceDto>>> getAllES() {
        logger.info("GET /api/es - Fetch all ES timeslots");
        List<EmployeeXServiceDto> timeslots = employeeXServiceService.getAllES();
        return ResponseEntity.ok(ApiResponse.success(timeslots, "ES timeslots retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeXServiceDto>> getESById(@PathVariable Long id) {
        logger.info("GET /api/es/{} - Fetch ES by id", id);
        try {
            EmployeeXServiceDto timeslot = employeeXServiceService.getESById(id);
            return ResponseEntity.ok(ApiResponse.success(timeslot, "ES timeslot retrieved successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeXServiceRepository.findAll().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "ES not found", availableIds));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeXServiceDto>> createES(@RequestBody EmployeeXServiceDto dto) {
        logger.info("POST /api/es - Create new ES timeslot");
        try {
            EmployeeXServiceDto created = employeeXServiceService.createES(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created, "ES timeslot created successfully"));
        } catch (ValidationException e) {
            logger.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                    .body(ApiResponse.error("Validation failed", e.getMessage()));
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Required entity not found"));
        } catch (Exception e) {
            logger.error("Error creating ES", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create ES", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeXServiceDto>> updateESById(@PathVariable Long id, @RequestBody EmployeeXServiceDto dto) {
        logger.info("PUT /api/es/{} - Update ES by path ID", id);
        try {
            EmployeeXServiceDto updated = employeeXServiceService.updateES(id, dto);
            return ResponseEntity.ok(ApiResponse.success(updated, "ES timeslot updated successfully"));
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            List<Long> availableIds = employeeXServiceRepository.findAll().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "ES not found", availableIds));
        } catch (ValidationException e) {
            logger.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                    .body(ApiResponse.error("Validation failed", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error updating ES", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update ES", e.getMessage()));
        }
    }

    @PutMapping("/entity")
    public ResponseEntity<ApiResponse<EmployeeXServiceDto>> updateESByEntity(@RequestBody IdRequestDto dto) {
        logger.info("PUT /api/es/entity - Update ES by entity ID");
        try {
            if (dto.getId() == null) {
                throw new IllegalArgumentException("Missing 'id' in request body");
            }
            EmployeeXServiceDto esDto = EmployeeXServiceDto.builder()
                    .id(dto.getId())
                    .startTime(dto.getStart() != null ? java.time.LocalTime.parse(dto.getStart()) : null)
                    .date(dto.getDate() != null ? java.time.LocalDate.parse(dto.getDate()) : null)
                    .x2(dto.getX2())
                    .status(dto.getStatus())
                    .build();
            EmployeeXServiceDto updated = employeeXServiceService.updateES(dto.getId(), esDto);
            return ResponseEntity.ok(ApiResponse.success(updated, "ES timeslot updated successfully"));
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            List<Long> availableIds = employeeXServiceRepository.findAll().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "ES not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating ES", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update ES", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteESById(@PathVariable Long id) {
        logger.info("DELETE /api/es/{} - Delete ES by ID", id);
        try {
            employeeXServiceService.deleteES(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            List<Long> availableIds = employeeXServiceRepository.findAll().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "ES not found", availableIds));
        } catch (Exception e) {
            logger.error("Error deleting ES", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to delete ES", e.getMessage()));
        }
    }

    @DeleteMapping("/entity")
    public ResponseEntity<ApiResponse<Void>> deleteESByEntity(@RequestBody IdRequestDto dto) {
        logger.info("DELETE /api/es/entity - Delete ES by entity ID");
        try {
            if (dto.getId() == null) {
                throw new IllegalArgumentException("Missing 'id' in request body");
            }
            employeeXServiceService.deleteES(dto.getId());
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            List<Long> availableIds = employeeXServiceRepository.findAll().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "ES not found", availableIds));
        } catch (Exception e) {
            logger.error("Error deleting ES", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to delete ES", e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> deleteAllES() {
        logger.warn("DELETE /api/es/clear - Delete all ES timeslots");
        try {
            employeeXServiceService.deleteAllES();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting all ES", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to delete all ES", e.getMessage()));
        }
    }

    @GetMapping("/free/{date}")
    public ResponseEntity<ApiResponse<List<EmployeeXServiceDto>>> getFreeESByDate(@PathVariable LocalDate date) {
        logger.info("GET /api/es/free/{} - Get free ES by date", date);
        try {
            List<EmployeeXServiceDto> freeTimeslots = employeeXServiceService.getFreeESByDate(date);
            return ResponseEntity.ok(ApiResponse.success(freeTimeslots, "Free ES timeslots retrieved successfully"));
        } catch (Exception e) {
            logger.error("Error fetching free ES", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to fetch free ES", e.getMessage()));
        }
    }
}
