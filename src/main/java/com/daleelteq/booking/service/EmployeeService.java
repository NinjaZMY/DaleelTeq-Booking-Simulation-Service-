package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.Employee;
import com.daleelteq.booking.dto.EmployeeDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    /**
     * Get all employees
     */
    public List<EmployeeDto> getAllEmployees() {
        log.debug("Fetching all employees");
        return employeeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get employee by ID
     */
    public EmployeeDto getEmployeeById(Long id) {
        log.debug("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableEmployeeIds();
                    log.warn("Employee not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("Employee with id %d not found. Available employee ids: %s", id, availableIds)
                    );
                });
        return toDto(employee);
    }

    /**
     * Create new employee
     */
    public EmployeeDto createEmployee(EmployeeDto dto) {
        log.info("Creating new employee: {}", dto.getLib());

        Employee employee = Employee.builder()
                .lib(dto.getLib())
                .build();

        Employee saved = employeeRepository.save(employee);
        log.info("Employee created successfully with id: {}", saved.getId());
        return toDto(saved);
    }

    /**
     * Update employee
     */
    public EmployeeDto updateEmployee(Long id, EmployeeDto dto) {
        log.info("Updating employee with id: {}", id);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableEmployeeIds();
                    log.warn("Employee not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("Employee with id %d not found. Available employee ids: %s", id, availableIds)
                    );
                });

        if (dto.getLib() != null) {
            employee.setLib(dto.getLib());
        }

        Employee updated = employeeRepository.save(employee);
        log.info("Employee updated successfully with id: {}", updated.getId());
        return toDto(updated);
    }

    /**
     * Delete employee
     */
    public void deleteEmployee(Long id) {
        log.info("Deleting employee with id: {}", id);

        if (!employeeRepository.existsById(id)) {
            String availableIds = getAvailableEmployeeIds();
            log.warn("Employee not found with id: {}. Available ids: {}", id, availableIds);
            throw new EntityNotFoundException(
                    String.format("Employee with id %d not found. Available employee ids: %s", id, availableIds)
            );
        }

        employeeRepository.deleteById(id);
        log.info("Employee deleted successfully with id: {}", id);
    }

    /**
     * Delete all employees
     */
    public void deleteAllEmployees() {
        log.warn("Deleting all employees");
        employeeRepository.deleteAll();
        log.info("All employees deleted");
    }

    /**
     * Helper method to get available employee IDs for error messages
     */
    private String getAvailableEmployeeIds() {
        return employeeRepository.findAll().stream()
                .map(e -> String.valueOf(e.getId()))
                .collect(Collectors.joining(",", "[", "]"));
    }

    /**
     * Convert entity to DTO
     */
    private EmployeeDto toDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .lib(employee.getLib())
                .createdAt(employee.getCreatedAt())
                .build();
    }
}
