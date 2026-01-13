package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.Service;
import com.daleelteq.booking.repository.ServiceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ServiceService serviceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetServiceById_Success() {
        Long serviceId = 1L;
        Service service = Service.builder()
                .id(serviceId)
                .lib("Haircut")
                .timeValue(15)
                .build();

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(service));

        var result = serviceService.getServiceById(serviceId);

        assertNotNull(result);
        assertEquals("Haircut", result.getLib());
        assertEquals(15, result.getTimeValue());
        verify(serviceRepository, times(1)).findById(serviceId);
    }

    @Test
    void testGetServiceById_NotFound() {
        Long serviceId = 999L;
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.empty());
        when(serviceRepository.findAll()).thenReturn(java.util.List.of());

        assertThrows(com.daleelteq.booking.exception.EntityNotFoundException.class, 
            () -> serviceService.getServiceById(serviceId));
    }

    @Test
    void testCreateService_Success() {
        var dto = com.daleelteq.booking.dto.ServiceDto.builder()
                .lib("Styling")
                .timeValue(20)
                .build();

        Service savedService = Service.builder()
                .id(1L)
                .lib("Styling")
                .timeValue(20)
                .build();

        when(serviceRepository.save(any(Service.class))).thenReturn(savedService);

        var result = serviceService.createService(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Styling", result.getLib());
    }

    @Test
    void testCreateService_InvalidTimeValue() {
        var dto = com.daleelteq.booking.dto.ServiceDto.builder()
                .lib("InvalidService")
                .timeValue(45)
                .build();

        assertThrows(IllegalArgumentException.class, () -> serviceService.createService(dto));
    }

    @Test
    void testCreateService_ValidTimeValues() {
        int[] validValues = {15, 20, 25, 30};
        
        for (int timeValue : validValues) {
            var dto = com.daleelteq.booking.dto.ServiceDto.builder()
                    .lib("Service" + timeValue)
                    .timeValue(timeValue)
                    .build();

            Service savedService = Service.builder()
                    .id((long)timeValue)
                    .lib("Service" + timeValue)
                    .timeValue(timeValue)
                    .build();

            when(serviceRepository.save(any(Service.class))).thenReturn(savedService);

            var result = serviceService.createService(dto);
            assertNotNull(result);
            assertEquals(timeValue, result.getTimeValue());
        }
    }
}
