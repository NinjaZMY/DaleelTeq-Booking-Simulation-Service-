package com.daleelteq.booking.controller;

import com.daleelteq.booking.dto.EmployeeDto;
import com.daleelteq.booking.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PostMapping
    public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(employeeService.createEmployee(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDto> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    @PutMapping("/entity")
    public ResponseEntity<EmployeeDto> updateEmployeeByEntity(@RequestBody EmployeeDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Missing 'id' in request body for update. Available employee ids: " +
                    employeeService.getAllEmployees().stream()
                            .map(e -> String.valueOf(e.getId()))
                            .toList()
            );
        }
        return ResponseEntity.ok(employeeService.updateEmployee(dto.getId(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/entity")
    public ResponseEntity<Void> deleteEmployeeByEntity(@RequestBody EmployeeDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Missing 'id' in request body for delete. Available employee ids: " +
                    employeeService.getAllEmployees().stream()
                            .map(e -> String.valueOf(e.getId()))
                            .toList()
            );
        }
        employeeService.deleteEmployee(dto.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllEmployees() {
        employeeService.deleteAllEmployees();
        return ResponseEntity.noContent().build();
    }
}
