package com.daleelteq.booking.controller;

import com.daleelteq.booking.dto.ServiceDto;
import com.daleelteq.booking.service.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ui/services")
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @GetMapping
    public ResponseEntity<List<ServiceDto>> getAllServices() {
        return ResponseEntity.ok(serviceService.getAllServices());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceDto> getServiceById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @PostMapping
    public ResponseEntity<ServiceDto> createService(@RequestBody ServiceDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(serviceService.createService(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceDto> updateService(@PathVariable Long id, @RequestBody ServiceDto dto) {
        return ResponseEntity.ok(serviceService.updateService(id, dto));
    }

    @PutMapping("/entity")
    public ResponseEntity<ServiceDto> updateServiceByEntity(@RequestBody ServiceDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Missing 'id' in request body for update. Available service ids: " +
                    serviceService.getAllServices().stream()
                            .map(s -> String.valueOf(s.getId()))
                            .toList()
            );
        }
        return ResponseEntity.ok(serviceService.updateService(dto.getId(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/entity")
    public ResponseEntity<Void> deleteServiceByEntity(@RequestBody ServiceDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Missing 'id' in request body for delete. Available service ids: " +
                    serviceService.getAllServices().stream()
                            .map(s -> String.valueOf(s.getId()))
                            .toList()
            );
        }
        serviceService.deleteService(dto.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllServices() {
        serviceService.deleteAllServices();
        return ResponseEntity.noContent().build();
    }
}
