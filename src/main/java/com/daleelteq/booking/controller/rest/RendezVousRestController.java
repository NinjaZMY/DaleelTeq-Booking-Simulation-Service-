package com.daleelteq.booking.controller.rest;

import com.daleelteq.booking.dto.ApiResponse;
import com.daleelteq.booking.dto.RendezVousDto;
import com.daleelteq.booking.dto.IdRequestDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.exception.ValidationException;
import com.daleelteq.booking.service.RendezVousService;
import com.daleelteq.booking.repository.RendezVousRepository;
import com.daleelteq.booking.repository.EmployeeXServiceRepository;
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
    private final EmployeeXServiceRepository employeeXServiceRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<RendezVousDto>>> getAllRendezVous() {
        logger.info("GET /api/rendezvous - Fetch all rendez-vous");
        List<RendezVousDto> bookings = rendezVousService.getAllRendezVous();
        return ResponseEntity.ok(ApiResponse.success(bookings, "Rendez-vous retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RendezVousDto>> getRendezVousById(@PathVariable Long id) {
        logger.info("GET /api/rendezvous/{} - Fetch rendez-vous by id", id);
        try {
            RendezVousDto booking = rendezVousService.getRendezVousById(id);
            return ResponseEntity.ok(ApiResponse.success(booking, "Rendez-vous retrieved successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = rendezVousRepository.findAll().stream()
                    .map(rv -> rv.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Rendez-vous not found", availableIds));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<RendezVousDto>> bookRendezVous(@RequestBody IdRequestDto dto) {
        logger.info("POST /api/rendezvous - Book appointment for ES: {}, Client: {}", dto.getIdES(), dto.getIdC());
        try {
            if (dto.getIdES() == null || dto.getIdC() == null) {
                throw new IllegalArgumentException("Missing 'idES' or 'idC' in request body");
            }
            RendezVousDto booked = rendezVousService.bookRendezVous(dto.getIdES(), dto.getIdC());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(booked, "Appointment booked successfully"));
        } catch (ValidationException e) {
            logger.warn("Validation error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                    .body(ApiResponse.error("Validation failed", e.getMessage()));
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            List<Long> freeIds = employeeXServiceRepository.findByStatus("free").stream()
                    .map(es -> es.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Required entity not found", freeIds));
        } catch (Exception e) {
            logger.error("Error booking rendez-vous", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to book appointment", e.getMessage()));
        }
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<RendezVousDto>> cancelRendezVous(
            @PathVariable Long id,
            @RequestBody IdRequestDto dto) {
        logger.info("PATCH /api/rendezvous/{}/cancel - Cancel rendez-vous", id);
        try {
            String cancelledBy = dto.getStatus() != null ? dto.getStatus() : "Client";
            RendezVousDto cancelled = rendezVousService.cancelRendezVous(id, cancelledBy);
            return ResponseEntity.ok(ApiResponse.success(cancelled, "Rendez-vous cancelled successfully"));
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            List<Long> availableIds = rendezVousRepository.findAll().stream()
                    .map(rv -> rv.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Rendez-vous not found", availableIds));
        } catch (Exception e) {
            logger.error("Error cancelling rendez-vous", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to cancel rendez-vous", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteRendezVousById(@PathVariable Long id) {
        logger.info("DELETE /api/rendezvous/{} - Delete rendez-vous by ID", id);
        try {
            rendezVousService.deleteRendezVous(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            List<Long> availableIds = rendezVousRepository.findAll().stream()
                    .map(rv -> rv.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Rendez-vous not found", availableIds));
        } catch (Exception e) {
            logger.error("Error deleting rendez-vous", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to delete rendez-vous", e.getMessage()));
        }
    }

    @DeleteMapping("/entity")
    public ResponseEntity<ApiResponse<Void>> deleteRendezVousByEntity(@RequestBody IdRequestDto dto) {
        logger.info("DELETE /api/rendezvous/entity - Delete rendez-vous by entity ID");
        try {
            if (dto.getId() == null) {
                throw new IllegalArgumentException("Missing 'id' in request body");
            }
            rendezVousService.deleteRendezVous(dto.getId());
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            logger.warn("Entity not found: {}", e.getMessage());
            List<Long> availableIds = rendezVousRepository.findAll().stream()
                    .map(rv -> rv.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Rendez-vous not found", availableIds));
        } catch (Exception e) {
            logger.error("Error deleting rendez-vous", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to delete rendez-vous", e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> deleteAllRendezVous() {
        logger.warn("DELETE /api/rendezvous/clear - Delete all rendez-vous");
        try {
            rendezVousService.deleteAllRendezVous();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            logger.error("Error deleting all rendez-vous", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to delete all rendez-vous", e.getMessage()));
        }
    }
}
