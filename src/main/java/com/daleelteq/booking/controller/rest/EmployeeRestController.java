package com.daleelteq.booking.controller.rest;

import com.daleelteq.booking.dto.ApiResponse;
import com.daleelteq.booking.dto.EmployeeDto;
import com.daleelteq.booking.dto.IdRequestDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.service.EmployeeService;
import com.daleelteq.booking.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeRestController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeRestController.class);
    private final EmployeeService employeeService;
    private final EmployeeRepository employeeRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<EmployeeDto>>> getAllEmployees() {
        logger.info("GET /api/employees - Fetch all employees");
        List<EmployeeDto> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(ApiResponse.success(employees, "Employees retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDto>> getEmployeeById(@PathVariable Long id) {
        logger.info("GET /api/employees/{} - Fetch employee by id", id);
        try {
            EmployeeDto employee = employeeService.getEmployeeById(id);
            return ResponseEntity.ok(ApiResponse.success(employee, "Employee retrieved successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeRepository.findAll().stream()
                    .map(emp -> emp.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Employee not found", availableIds));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeDto>> createEmployee(@RequestBody EmployeeDto dto) {
        logger.info("POST /api/employees - Create new employee: {}", dto.getLib());
        try {
            EmployeeDto created = employeeService.createEmployee(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created, "Employee created successfully"));
        } catch (Exception e) {
            logger.error("Error creating employee", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create employee", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployeeDto>> updateEmployeeById(@PathVariable Long id, @RequestBody EmployeeDto dto) {
        logger.info("PUT /api/employees/{} - Update employee", id);
        try {
            EmployeeDto updated = employeeService.updateEmployee(id, dto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Employee updated successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeRepository.findAll().stream()
                    .map(emp -> emp.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Employee not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating employee", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update employee", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<EmployeeDto>> updateEmployeeByBody(@RequestBody IdRequestDto dto) {
        logger.info("PUT /api/employees (JSON body) - Update employee with id: {}", dto.getId());
        try {
            EmployeeDto employeeDto = EmployeeDto.builder()
                    .lib(dto.getLib())
                    .build();
            EmployeeDto updated = employeeService.updateEmployee(dto.getId(), employeeDto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Employee updated successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeRepository.findAll().stream()
                    .map(emp -> emp.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Employee not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating employee", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update employee", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEmployeeById(@PathVariable Long id) {
        logger.info("DELETE /api/employees/{} - Delete employee", id);
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeRepository.findAll().stream()
                    .map(emp -> emp.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Employee not found", availableIds));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteEmployeeByBody(@RequestBody IdRequestDto dto) {
        logger.info("DELETE /api/employees (JSON body) - Delete employee with id: {}", dto.getId());
        try {
            employeeService.deleteEmployee(dto.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = employeeRepository.findAll().stream()
                    .map(emp -> emp.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Employee not found", availableIds));
        }
    }
}

