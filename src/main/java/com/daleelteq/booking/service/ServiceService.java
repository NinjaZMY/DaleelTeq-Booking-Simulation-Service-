package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.Service;
import com.daleelteq.booking.dto.ServiceDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.exception.ValidationException;
import com.daleelteq.booking.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServiceService {

    private static final Logger logger = LoggerFactory.getLogger(ServiceService.class);
    private final ServiceRepository serviceRepository;

    @Transactional(readOnly = true)
    public List<ServiceDto> getAllServices() {
        logger.info("Fetching all services");
        return serviceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ServiceDto getServiceById(Long id) {
        logger.info("Fetching service with id: {}", id);
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service", id));
        return convertToDto(service);
    }

    @Transactional
    public ServiceDto createService(ServiceDto dto) {
        validateTimeValue(dto.getTimeValue());
        logger.info("Creating service: {}", dto.getLib());
        
        Service service = Service.builder()
                .lib(dto.getLib())
                .timeValue(dto.getTimeValue())
                .build();
        
        Service saved = serviceRepository.save(service);
        logger.info("Service created with id: {}", saved.getId());
        return convertToDto(saved);
    }

    @Transactional
    public ServiceDto updateService(Long id, ServiceDto dto) {
        validateTimeValue(dto.getTimeValue());
        logger.info("Updating service with id: {}", id);
        
        Service service = serviceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Service", id));
        
        service.setLib(dto.getLib());
        service.setTimeValue(dto.getTimeValue());
        
        Service updated = serviceRepository.save(service);
        logger.info("Service updated with id: {}", id);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteService(Long id) {
        logger.info("Deleting service with id: {}", id);
        
        if (!serviceRepository.existsById(id)) {
            throw new EntityNotFoundException("Service", id);
        }
        
        serviceRepository.deleteById(id);
        logger.info("Service deleted with id: {}", id);
    }

    private void validateTimeValue(Integer timeValue) {
        if (timeValue == null || (!timeValue.equals(15) && !timeValue.equals(20) && !timeValue.equals(25) && !timeValue.equals(30))) {
            throw new ValidationException("timeValue", "ALLOWED_VALUES", 
                    "Time value must be one of: 15, 20, 25, 30. Provided: " + timeValue);
        }
    }

    private ServiceDto convertToDto(Service service) {
        return ServiceDto.builder()
                .id(service.getId())
                .lib(service.getLib())
                .timeValue(service.getTimeValue())
                .build();
    }
}

