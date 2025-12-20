package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.RendezVous;
import com.daleelteq.booking.domain.EmployeeService;
import com.daleelteq.booking.domain.Client;
import com.daleelteq.booking.domain.Notification;
import com.daleelteq.booking.dto.RendezVousDto;
import com.daleelteq.booking.dto.NotificationDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.exception.BusinessRuleException;
import com.daleelteq.booking.repository.RendezVousRepository;
import com.daleelteq.booking.repository.EmployeeServiceRepository;
import com.daleelteq.booking.repository.ClientRepository;
import com.daleelteq.booking.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RendezVousService {

    private static final Logger logger = LoggerFactory.getLogger(RendezVousService.class);
    private final RendezVousRepository rendezVousRepository;
    private final EmployeeServiceRepository employeeServiceRepository;
    private final ClientRepository clientRepository;
    private final NotificationRepository notificationRepository;

    @Transactional(readOnly = true)
    public List<RendezVousDto> getAllRendezVous() {
        logger.info("Fetching all rendezvous");
        return rendezVousRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RendezVousDto getRendezVousById(Long id) {
        logger.info("Fetching rendezvous with id: {}", id);
        RendezVous rv = rendezVousRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RendezVous", id));
        return convertToDto(rv);
    }

    @Transactional
    public RendezVousDto bookRendezVous(Long employeeServiceId, Long clientId) {
        logger.info("Booking rendezvous for ES id: {}, Client id: {}", employeeServiceId, clientId);
        
        // Validate EmployeeService exists
        EmployeeService es = employeeServiceRepository.findById(employeeServiceId)
                .orElseThrow(() -> new EntityNotFoundException("EmployeeService", employeeServiceId));
        
        // Validate Client exists
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client", clientId));
        
        // Validate ES is free
        if (!"free".equalsIgnoreCase(es.getStatus())) {
            List<Long> freeIds = employeeServiceRepository.findAllFree().stream()
                    .map(EmployeeService::getId)
                    .collect(Collectors.toList());
            throw new BusinessRuleException("ES_STATUS", 
                    "Employee service is not available (status: " + es.getStatus() + "). Available slot IDs: " + freeIds);
        }
        
        // Create RendezVous with status "Active"
        RendezVous rv = RendezVous.builder()
                .employeeService(es)
                .client(client)
                .status("Active")
                .build();
        
        RendezVous savedRv = rendezVousRepository.save(rv);
        logger.info("RendezVous created with id: {}", savedRv.getId());
        
        // Set EmployeeService status to "taken"
        es.setStatus("taken");
        employeeServiceRepository.save(es);
        logger.info("EmployeeService id {} status set to 'taken'", employeeServiceId);
        
        // Create Notification
        Notification notification = Notification.builder()
                .rendezVous(savedRv)
                .type("booked")
                .value("Appointment booked successfully. Status: " + savedRv.getStatus() + (es.getX2() ? " (double duration)" : ""))
                .build();
        
        notificationRepository.save(notification);
        logger.info("Notification created for RendezVous id: {}", savedRv.getId());
        
        return convertToDto(savedRv);
    }

    @Transactional
    public RendezVousDto cancelRendezVous(Long rendezVousId, String cancelledBy) {
        logger.info("Cancelling rendezvous id: {} by: {}", rendezVousId, cancelledBy);
        
        RendezVous rv = rendezVousRepository.findById(rendezVousId)
                .orElseThrow(() -> new EntityNotFoundException("RendezVous", rendezVousId));
        
        // Check if already cancelled
        if (rv.getStatus().contains("Cancelled")) {
            throw new BusinessRuleException("RV_STATUS", "RendezVous is already cancelled");
        }
        
        // Determine status based on who cancelled
        String newStatus = "Client".equalsIgnoreCase(cancelledBy) ? 
                "Cancelled by Client" : "Cancelled by Employee";
        
        rv.setStatus(newStatus);
        rv.setCancelledAt(java.time.LocalDateTime.now());
        
        RendezVous updatedRv = rendezVousRepository.save(rv);
        logger.info("RendezVous id {} cancelled by: {}", rendezVousId, cancelledBy);
        
        // Set related EmployeeService status back to "free"
        EmployeeService es = rv.getEmployeeService();
        es.setStatus("free");
        employeeServiceRepository.save(es);
        logger.info("EmployeeService id {} status set back to 'free'", es.getId());
        
        // Create Notification inheriting the cancellation status
        Notification notification = Notification.builder()
                .rendezVous(updatedRv)
                .type("cancelled")
                .value("Appointment " + newStatus + ". Reason inherited from cancellation.")
                .build();
        
        notificationRepository.save(notification);
        logger.info("Cancellation notification created for RendezVous id: {}", rendezVousId);
        
        return convertToDto(updatedRv);
    }

    @Transactional
    public RendezVousDto updateRendezVous(Long id, RendezVousDto dto) {
        logger.info("Updating rendezvous with id: {}", id);
        
        RendezVous rv = rendezVousRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("RendezVous", id));
        
        if (dto.getIdC() != null) {
            Client client = clientRepository.findById(dto.getIdC())
                    .orElseThrow(() -> new EntityNotFoundException("Client", dto.getIdC()));
            rv.setClient(client);
        }
        
        if (dto.getStatus() != null) {
            rv.setStatus(dto.getStatus());
        }
        
        RendezVous updated = rendezVousRepository.save(rv);
        logger.info("RendezVous updated with id: {}", id);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteRendezVous(Long id) {
        logger.info("Deleting rendezvous with id: {}", id);
        
        if (!rendezVousRepository.existsById(id)) {
            throw new EntityNotFoundException("RendezVous", id);
        }
        
        rendezVousRepository.deleteById(id);
        logger.info("RendezVous deleted with id: {}", id);
    }

    private RendezVousDto convertToDto(RendezVous rv) {
        return RendezVousDto.builder()
                .id(rv.getId())
                .idEs(rv.getEmployeeService().getId())
                .idC(rv.getClient().getId())
                .status(rv.getStatus())
                .createdAt(rv.getCreatedAt())
                .cancelledAt(rv.getCancelledAt())
                .build();
    }
}

