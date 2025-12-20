package com.daleelteq.booking.controller.rest;

import com.daleelteq.booking.dto.ApiResponse;
import com.daleelteq.booking.dto.RendezVousDto;
import com.daleelteq.booking.dto.IdRequestDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.exception.BusinessRuleException;
import com.daleelteq.booking.service.RendezVousService;
import com.daleelteq.booking.repository.RendezVousRepository;
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
@RequestMapping("/api/rendezvous")
@RequiredArgsConstructor
public class RendezVousRestController {

    private static final Logger logger = LoggerFactory.getLogger(RendezVousRestController.class);
    private final RendezVousService rendezVousService;
    private final RendezVousRepository rendezVousRepository;
    private final EmployeeServiceRepository employeeServiceRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RendezVousDto>>> getAllRendezVous() {
        logger.info("GET /api/rendezvous - Fetch all rendezvous");
        List<RendezVousDto> rendezvous = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(ApiResponse.success(rendezvous, "Rendezvous retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RendezVousDto>> getRendezVousById(@PathVariable Long id) {
        logger.info("GET /api/rendezvous/{} - Fetch rendezvous by id", id);
        try {
            RendezVousDto rendezvous = rendezVousService.getRendezVousById(id);
            return ResponseEntity.ok(ApiResponse.success(rendezvous, "Rendezvous retrieved successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = rendezVousRepository.findAll().stream()
                    .map(rv -> rv.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Rendezvous not found", availableIds));
        }
    }

    @PostMapping("/book")
    public ResponseEntity<ApiResponse<RendezVousDto>> bookRendezVous(
            @RequestParam Long employeeServiceId,
            @RequestParam Long clientId) {
        logger.info("POST /api/rendezvous/book - Book appointment for ES: {}, Client: {}", employeeServiceId, clientId);
        try {
            RendezVousDto booked = rendezVousService.bookRendezVous(employeeServiceId, clientId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(booked, "Appointment booked successfully"));
        } catch (BusinessRuleException e) {
            logger.warn("Business rule violation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Booking failed", e.getMessage()));
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            List<Long> freeIds = employeeServiceRepository.findAllFree().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Please check the ids", freeIds));
        } catch (Exception e) {
            logger.error("Error booking rendezvous", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to book appointment", e.getMessage()));
        }
    }

    @PostMapping("/book-json")
    public ResponseEntity<ApiResponse<RendezVousDto>> bookRendezVousJson(@RequestBody IdRequestDto dto) {
        logger.info("POST /api/rendezvous/book-json - Book appointment from JSON");
        try {
            RendezVousDto booked = rendezVousService.bookRendezVous(dto.getIdEs(), dto.getIdC());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(booked, "Appointment booked successfully"));
        } catch (BusinessRuleException e) {
            logger.warn("Business rule violation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Booking failed", e.getMessage()));
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            List<Long> freeIds = employeeServiceRepository.findAllFree().stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Please check the ids", freeIds));
        } catch (Exception e) {
            logger.error("Error booking rendezvous", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to book appointment", e.getMessage()));
        }
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<RendezVousDto>> cancelRendezVous(
            @PathVariable Long id,
            @RequestParam String cancelledBy) {
        logger.info("PUT /api/rendezvous/{}/cancel - Cancel rendezvous by: {}", id, cancelledBy);
        try {
            RendezVousDto cancelled = rendezVousService.cancelRendezVous(id, cancelledBy);
            return ResponseEntity.ok(ApiResponse.success(cancelled, "Appointment cancelled successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = rendezVousRepository.findAll().stream()
                    .map(rv -> rv.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Rendezvous not found", availableIds));
        } catch (BusinessRuleException e) {
            logger.warn("Business rule violation: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(ApiResponse.error("Cancellation failed", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error cancelling rendezvous", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to cancel appointment", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RendezVousDto>> updateRendezVousById(@PathVariable Long id, @RequestBody RendezVousDto dto) {
        logger.info("PUT /api/rendezvous/{} - Update rendezvous", id);
        try {
            RendezVousDto updated = rendezVousService.updateRendezVous(id, dto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Rendezvous updated successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = rendezVousRepository.findAll().stream()
                    .map(rv -> rv.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Rendezvous not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating rendezvous", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update rendezvous", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<RendezVousDto>> updateRendezVousByBody(@RequestBody IdRequestDto dto) {
        logger.info("PUT /api/rendezvous (JSON body) - Update rendezvous with id: {}", dto.getId());
        try {
            RendezVousDto rendezVousDto = RendezVousDto.builder()
                    .idC(dto.getIdC())
                    .status(dto.getStatus())
                    .build();
            RendezVousDto updated = rendezVousService.updateRendezVous(dto.getId(), rendezVousDto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Rendezvous updated successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = rendezVousRepository.findAll().stream()
                    .map(rv -> rv.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Rendezvous not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating rendezvous", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update rendezvous", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRendezVousById(@PathVariable Long id) {
        logger.info("DELETE /api/rendezvous/{} - Delete rendezvous", id);
        try {
            rendezVousService.deleteRendezVous(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = rendezVousRepository.findAll().stream()
                    .map(rv -> rv.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Rendezvous not found", availableIds));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteRendezVousByBody(@RequestBody IdRequestDto dto) {
        logger.info("DELETE /api/rendezvous (JSON body) - Delete rendezvous with id: {}", dto.getId());
        try {
            rendezVousService.deleteRendezVous(dto.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = rendezVousRepository.findAll().stream()
                    .map(rv -> rv.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Rendezvous not found", availableIds));
        }
    }
}

