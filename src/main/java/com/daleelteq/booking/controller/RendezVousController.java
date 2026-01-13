package com.daleelteq.booking.controller;

import com.daleelteq.booking.dto.RendezVousDto;
import com.daleelteq.booking.service.RendezVousService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/rendezvous")
@RequiredArgsConstructor
public class RendezVousController {

    private final RendezVousService rendezVousService;

    @GetMapping
    public ResponseEntity<List<RendezVousDto>> getAllRendezVous() {
        return ResponseEntity.ok(rendezVousService.getAllRendezVous());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RendezVousDto> getRendezVousById(@PathVariable Long id) {
        return ResponseEntity.ok(rendezVousService.getRendezVousById(id));
    }

    @PostMapping
    public ResponseEntity<RendezVousDto> bookRendezVous(@RequestBody Map<String, Long> request) {
        Long esId = request.get("idES");
        Long clientId = request.get("idC");

        if (esId == null || clientId == null) {
            throw new IllegalArgumentException("Request must include 'idES' and 'idC' fields");
        }

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(rendezVousService.bookRendezVous(esId, clientId));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<RendezVousDto> cancelRendezVous(
            @PathVariable Long id,
            @RequestBody Map<String, String> request) {
        String cancelledBy = request.getOrDefault("by", "unknown");
        return ResponseEntity.ok(rendezVousService.cancelRendezVous(id, cancelledBy));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRendezVous(@PathVariable Long id) {
        rendezVousService.deleteRendezVous(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/entity")
    public ResponseEntity<Void> deleteRendezVousByEntity(@RequestBody RendezVousDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Missing 'id' in request body for delete. Available rendez-vous ids: " +
                    rendezVousService.getAllRendezVous().stream()
                            .map(rv -> String.valueOf(rv.getId()))
                            .toList()
            );
        }
        rendezVousService.deleteRendezVous(dto.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllRendezVous() {
        rendezVousService.deleteAllRendezVous();
        return ResponseEntity.noContent().build();
    }
}
