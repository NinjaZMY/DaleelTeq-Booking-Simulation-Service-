package com.daleelteq.booking.controller;

import com.daleelteq.booking.dto.EmployeeXServiceDto;
import com.daleelteq.booking.service.EmployeeXServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/es")
@RequiredArgsConstructor
public class EmployeeXServiceController {

    private final EmployeeXServiceService esService;

    @GetMapping
    public ResponseEntity<List<EmployeeXServiceDto>> getAllES() {
        return ResponseEntity.ok(esService.getAllES());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeXServiceDto> getESById(@PathVariable Long id) {
        return ResponseEntity.ok(esService.getESById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeXServiceDto> createES(@RequestBody EmployeeXServiceDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(esService.createES(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeXServiceDto> updateES(@PathVariable Long id, @RequestBody EmployeeXServiceDto dto) {
        return ResponseEntity.ok(esService.updateES(id, dto));
    }

    @PutMapping("/entity")
    public ResponseEntity<EmployeeXServiceDto> updateESByEntity(@RequestBody EmployeeXServiceDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Missing 'id' in request body for update. Available ES ids: " +
                    esService.getAllES().stream()
                            .map(es -> String.valueOf(es.getId()))
                            .toList()
            );
        }
        return ResponseEntity.ok(esService.updateES(dto.getId(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteES(@PathVariable Long id) {
        esService.deleteES(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/entity")
    public ResponseEntity<Void> deleteESByEntity(@RequestBody EmployeeXServiceDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Missing 'id' in request body for delete. Available ES ids: " +
                    esService.getAllES().stream()
                            .map(es -> String.valueOf(es.getId()))
                            .toList()
            );
        }
        esService.deleteES(dto.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllES() {
        esService.deleteAllES();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/free/{date}")
    public ResponseEntity<List<EmployeeXServiceDto>> getFreeESByDate(@PathVariable LocalDate date) {
        return ResponseEntity.ok(esService.getFreeESByDate(date));
    }
}
