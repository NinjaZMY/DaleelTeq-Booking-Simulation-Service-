package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.Employee;
import com.daleelteq.booking.dto.EmployeeDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);
    private final EmployeeRepository employeeRepository;

    @Transactional(readOnly = true)
    public List<EmployeeDto> getAllEmployees() {
        logger.info("Fetching all employees");
        return employeeRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeDto getEmployeeById(Long id) {
        logger.info("Fetching employee with id: {}", id);
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee", id));
        return convertToDto(employee);
    }

    @Transactional
    public EmployeeDto createEmployee(EmployeeDto dto) {
        logger.info("Creating employee: {}", dto.getLib());
        
        Employee employee = Employee.builder()
                .lib(dto.getLib())
                .build();
        
        Employee saved = employeeRepository.save(employee);
        logger.info("Employee created with id: {}", saved.getId());
        return convertToDto(saved);
    }

    @Transactional
    public EmployeeDto updateEmployee(Long id, EmployeeDto dto) {
        logger.info("Updating employee with id: {}", id);
        
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Employee", id));
        
        employee.setLib(dto.getLib());
        
        Employee updated = employeeRepository.save(employee);
        logger.info("Employee updated with id: {}", id);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteEmployee(Long id) {
        logger.info("Deleting employee with id: {}", id);
        
        if (!employeeRepository.existsById(id)) {
            throw new EntityNotFoundException("Employee", id);
        }
        
        employeeRepository.deleteById(id);
        logger.info("Employee deleted with id: {}", id);
    }

    private EmployeeDto convertToDto(Employee employee) {
        return EmployeeDto.builder()
                .id(employee.getId())
                .lib(employee.getLib())
                .build();
    }
}

