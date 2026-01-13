package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.Client;
import com.daleelteq.booking.dto.ClientDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.repository.ClientRepository;
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
public class ClientService {

    private final ClientRepository clientRepository;

    /**
     * Get all clients
     */
    public List<ClientDto> getAllClients() {
        log.debug("Fetching all clients");
        return clientRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Get client by ID
     */
    public ClientDto getClientById(Long id) {
        log.debug("Fetching client with id: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableClientIds();
                    log.warn("Client not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("Client with id %d not found. Available client ids: %s", id, availableIds)
                    );
                });
        return toDto(client);
    }

    /**
     * Create new client
     */
    public ClientDto createClient(ClientDto dto) {
        log.info("Creating new client: {}", dto.getLib());

        Client client = Client.builder()
                .lib(dto.getLib())
                .number(dto.getNumber())
                .build();

        Client saved = clientRepository.save(client);
        log.info("Client created successfully with id: {}", saved.getId());
        return toDto(saved);
    }

    /**
     * Update client
     */
    public ClientDto updateClient(Long id, ClientDto dto) {
        log.info("Updating client with id: {}", id);

        Client client = clientRepository.findById(id)
                .orElseThrow(() -> {
                    String availableIds = getAvailableClientIds();
                    log.warn("Client not found with id: {}. Available ids: {}", id, availableIds);
                    return new EntityNotFoundException(
                            String.format("Client with id %d not found. Available client ids: %s", id, availableIds)
                    );
                });

        if (dto.getLib() != null) {
            client.setLib(dto.getLib());
        }
        if (dto.getNumber() != null) {
            client.setNumber(dto.getNumber());
        }

        Client updated = clientRepository.save(client);
        log.info("Client updated successfully with id: {}", updated.getId());
        return toDto(updated);
    }

    /**
     * Delete client
     */
    public void deleteClient(Long id) {
        log.info("Deleting client with id: {}", id);

        if (!clientRepository.existsById(id)) {
            String availableIds = getAvailableClientIds();
            log.warn("Client not found with id: {}. Available ids: {}", id, availableIds);
            throw new EntityNotFoundException(
                    String.format("Client with id %d not found. Available client ids: %s", id, availableIds)
            );
        }

        clientRepository.deleteById(id);
        log.info("Client deleted successfully with id: {}", id);
    }

    /**
     * Delete all clients
     */
    public void deleteAllClients() {
        log.warn("Deleting all clients");
        clientRepository.deleteAll();
        log.info("All clients deleted");
    }

    /**
     * Helper method to get available client IDs for error messages
     */
    private String getAvailableClientIds() {
        return clientRepository.findAll().stream()
                .map(c -> String.valueOf(c.getId()))
                .collect(Collectors.joining(",", "[", "]"));
    }

    /**
     * Convert entity to DTO
     */
    private ClientDto toDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .lib(client.getLib())
                .number(client.getNumber())
                .createdAt(client.getCreatedAt())
                .build();
    }
}
