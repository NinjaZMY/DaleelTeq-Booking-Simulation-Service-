package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.EmployeeService;
import com.daleelteq.booking.domain.Employee;
import com.daleelteq.booking.domain.Service;
import com.daleelteq.booking.dto.EmployeeServiceDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.exception.ValidationException;
import com.daleelteq.booking.repository.EmployeeServiceRepository;
import com.daleelteq.booking.repository.EmployeeRepository;
import com.daleelteq.booking.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeServiceService.class);
    private final EmployeeServiceRepository employeeServiceRepository;
    private final EmployeeRepository employeeRepository;
    private final ServiceRepository serviceRepository;

    private static final LocalTime WORK_START = LocalTime.of(8, 0);
    private static final LocalTime WORK_END = LocalTime.of(17, 0);

    @Transactional(readOnly = true)
    public List<EmployeeServiceDto> getAllEmployeeServices() {
        logger.info("Fetching all employee services");
        return employeeServiceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EmployeeServiceDto> getFreeEmployeeServices() {
        logger.info("Fetching all free employee services");
        return employeeServiceRepository.findAllFree().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public EmployeeServiceDto getEmployeeServiceById(Long id) {
        logger.info("Fetching employee service with id: {}", id);
        EmployeeService es = employeeServiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EmployeeService", id));
        return convertToDto(es);
    }

    @Transactional
    public EmployeeServiceDto createEmployeeService(EmployeeServiceDto dto) {
        logger.info("Creating employee service for employee id: {}, service id: {}", dto.getIdE(), dto.getIdS());
        
        // Validate employee exists
        Employee employee = employeeRepository.findById(dto.getIdE())
                .orElseThrow(() -> new EntityNotFoundException("Employee", dto.getIdE()));
        
        // Validate service exists
        com.daleelteq.booking.domain.Service service = serviceRepository.findById(dto.getIdS())
                .orElseThrow(() -> new EntityNotFoundException("Service", dto.getIdS()));
        
        // Validate time values
        validateTimeRange(dto.getStart(), dto.getEnd());
        validateDateNotNull(dto.getDate());
        
        // Compute effective time value
        Integer effectiveTimeValue = service.getTimeValue() * (dto.getX2() != null && dto.getX2() ? 2 : 1);
        
        EmployeeService es = EmployeeService.builder()
                .employee(employee)
                .service(service)
                .x2(dto.getX2() != null && dto.getX2())
                .start(dto.getStart())
                .end(dto.getEnd())
                .date(dto.getDate())
                .status("free")
                .timeValue(effectiveTimeValue)
                .build();
        
        EmployeeService saved = employeeServiceRepository.save(es);
        logger.info("Employee service created with id: {}", saved.getId());
        return convertToDto(saved);
    }

    @Transactional
    public EmployeeServiceDto updateEmployeeService(Long id, EmployeeServiceDto dto) {
        logger.info("Updating employee service with id: {}", id);
        
        EmployeeService es = employeeServiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EmployeeService", id));
        
        if (dto.getIdE() != null) {
            Employee employee = employeeRepository.findById(dto.getIdE())
                    .orElseThrow(() -> new EntityNotFoundException("Employee", dto.getIdE()));
            es.setEmployee(employee);
        }
        
        if (dto.getIdS() != null) {
            com.daleelteq.booking.domain.Service service = serviceRepository.findById(dto.getIdS())
                    .orElseThrow(() -> new EntityNotFoundException("Service", dto.getIdS()));
            es.setService(service);
        }
        
        if (dto.getStart() != null && dto.getEnd() != null) {
            validateTimeRange(dto.getStart(), dto.getEnd());
            es.setStart(dto.getStart());
            es.setEnd(dto.getEnd());
        }
        
        if (dto.getDate() != null) {
            es.setDate(dto.getDate());
        }
        
        if (dto.getX2() != null) {
            es.setX2(dto.getX2());
            Integer effectiveTimeValue = es.getService().getTimeValue() * (dto.getX2() ? 2 : 1);
            es.setTimeValue(effectiveTimeValue);
        }
        
        if (dto.getStatus() != null) {
            es.setStatus(dto.getStatus());
        }
        
        EmployeeService updated = employeeServiceRepository.save(es);
        logger.info("Employee service updated with id: {}", id);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteEmployeeService(Long id) {
        logger.info("Deleting employee service with id: {}", id);
        
        if (!employeeServiceRepository.existsById(id)) {
            throw new EntityNotFoundException("EmployeeService", id);
        }
        
        employeeServiceRepository.deleteById(id);
        logger.info("Employee service deleted with id: {}", id);
    }

    private void validateTimeRange(LocalTime start, LocalTime end) {
        if (start == null || end == null) {
            throw new ValidationException("start/end", "NOT_NULL", "Start and end times cannot be null");
        }
        
        if (start.isBefore(WORK_START)) {
            throw new ValidationException("start", "TIME_RANGE", 
                    "Start time must be between 08:00 and 17:00. You provided: " + start);
        }
        
        if (end.isAfter(WORK_END)) {
            throw new ValidationException("end", "TIME_RANGE", 
                    "End time must be between 08:00 and 17:00. You provided: " + end);
        }
        
        if (!start.isBefore(end)) {
            throw new ValidationException("start/end", "ORDER", 
                    "Start time ("+start+") must be before end time ("+end+")");
        }
    }

    private void validateDateNotNull(LocalDate date) {
        if (date == null) {
            throw new ValidationException("date", "NOT_NULL", "Date cannot be null (format: YYYY-MM-DD)");
        }
    }

    private EmployeeServiceDto convertToDto(EmployeeService es) {
        return EmployeeServiceDto.builder()
                .id(es.getId())
                .idE(es.getEmployee().getId())
                .idS(es.getService().getId())
                .x2(es.getX2())
                .start(es.getStart())
                .end(es.getEnd())
                .date(es.getDate())
                .status(es.getStatus())
                .timeValue(es.getTimeValue())
                .employee(convertEmployeeToDto(es.getEmployee()))
                .service(convertServiceToDto(es.getService()))
                .build();
    }

    private EmployeeServiceDto convertToSimpleDto(EmployeeService es) {
        return EmployeeServiceDto.builder()
                .id(es.getId())
                .idE(es.getEmployee().getId())
                .idS(es.getService().getId())
                .x2(es.getX2())
                .start(es.getStart())
                .end(es.getEnd())
                .date(es.getDate())
                .status(es.getStatus())
                .timeValue(es.getTimeValue())
                .build();
    }

    private EmployeeServiceService.EmployeeDto convertEmployeeToDto(Employee e) {
        return EmployeeServiceService.EmployeeDto.builder()
                .id(e.getId())
                .lib(e.getLib())
                .build();
    }

    private EmployeeServiceService.ServiceDto convertServiceToDto(Service s) {
        return EmployeeServiceService.ServiceDto.builder()
                .id(s.getId())
                .lib(s.getLib())
                .timeValue(s.getTimeValue())
                .build();
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class EmployeeDto {
        private Long id;
        private String lib;
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    @lombok.Builder
    public static class ServiceDto {
        private Long id;
        private String lib;
        private Integer timeValue;
    }
}

