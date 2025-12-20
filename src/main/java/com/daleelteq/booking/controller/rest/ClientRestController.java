package com.daleelteq.booking.controller.rest;

import com.daleelteq.booking.dto.ApiResponse;
import com.daleelteq.booking.dto.ClientDto;
import com.daleelteq.booking.dto.IdRequestDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.service.ClientService;
import com.daleelteq.booking.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class ClientRestController {

    private static final Logger logger = LoggerFactory.getLogger(ClientRestController.class);
    private final ClientService clientService;
    private final ClientRepository clientRepository;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ClientDto>>> getAllClients() {
        logger.info("GET /api/clients - Fetch all clients");
        List<ClientDto> clients = clientService.getAllClients();
        return ResponseEntity.ok(ApiResponse.success(clients, "Clients retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDto>> getClientById(@PathVariable Long id) {
        logger.info("GET /api/clients/{} - Fetch client by id", id);
        try {
            ClientDto client = clientService.getClientById(id);
            return ResponseEntity.ok(ApiResponse.success(client, "Client retrieved successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = clientRepository.findAll().stream()
                    .map(c -> c.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Client not found", availableIds));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ClientDto>> createClient(@RequestBody ClientDto dto) {
        logger.info("POST /api/clients - Create new client: {}", dto.getLib());
        try {
            ClientDto created = clientService.createClient(dto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success(created, "Client created successfully"));
        } catch (Exception e) {
            logger.error("Error creating client", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to create client", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ClientDto>> updateClientById(@PathVariable Long id, @RequestBody ClientDto dto) {
        logger.info("PUT /api/clients/{} - Update client", id);
        try {
            ClientDto updated = clientService.updateClient(id, dto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Client updated successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = clientRepository.findAll().stream()
                    .map(c -> c.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Client not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating client", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update client", e.getMessage()));
        }
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ClientDto>> updateClientByBody(@RequestBody IdRequestDto dto) {
        logger.info("PUT /api/clients (JSON body) - Update client with id: {}", dto.getId());
        try {
            ClientDto clientDto = ClientDto.builder()
                    .lib(dto.getLib())
                    .number(dto.getNumber())
                    .build();
            ClientDto updated = clientService.updateClient(dto.getId(), clientDto);
            return ResponseEntity.ok(ApiResponse.success(updated, "Client updated successfully"));
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = clientRepository.findAll().stream()
                    .map(c -> c.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Client not found", availableIds));
        } catch (Exception e) {
            logger.error("Error updating client", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error("Failed to update client", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteClientById(@PathVariable Long id) {
        logger.info("DELETE /api/clients/{} - Delete client", id);
        try {
            clientService.deleteClient(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = clientRepository.findAll().stream()
                    .map(c -> c.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Client not found", availableIds));
        }
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> deleteClientByBody(@RequestBody IdRequestDto dto) {
        logger.info("DELETE /api/clients (JSON body) - Delete client with id: {}", dto.getId());
        try {
            clientService.deleteClient(dto.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .build();
        } catch (EntityNotFoundException e) {
            List<Long> availableIds = clientRepository.findAll().stream()
                    .map(c -> c.getId())
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage(), "Client not found", availableIds));
        }
    }
}

