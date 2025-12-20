package com.daleelteq.booking.service;

import com.daleelteq.booking.domain.Client;
import com.daleelteq.booking.dto.ClientDto;
import com.daleelteq.booking.exception.EntityNotFoundException;
import com.daleelteq.booking.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientService {

    private static final Logger logger = LoggerFactory.getLogger(ClientService.class);
    private final ClientRepository clientRepository;

    @Transactional(readOnly = true)
    public List<ClientDto> getAllClients() {
        logger.info("Fetching all clients");
        return clientRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClientDto getClientById(Long id) {
        logger.info("Fetching client with id: {}", id);
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client", id));
        return convertToDto(client);
    }

    @Transactional
    public ClientDto createClient(ClientDto dto) {
        logger.info("Creating client: {}", dto.getLib());
        
        Client client = Client.builder()
                .lib(dto.getLib())
                .number(dto.getNumber())
                .build();
        
        Client saved = clientRepository.save(client);
        logger.info("Client created with id: {}", saved.getId());
        return convertToDto(saved);
    }

    @Transactional
    public ClientDto updateClient(Long id, ClientDto dto) {
        logger.info("Updating client with id: {}", id);
        
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Client", id));
        
        client.setLib(dto.getLib());
        client.setNumber(dto.getNumber());
        
        Client updated = clientRepository.save(client);
        logger.info("Client updated with id: {}", id);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteClient(Long id) {
        logger.info("Deleting client with id: {}", id);
        
        if (!clientRepository.existsById(id)) {
            throw new EntityNotFoundException("Client", id);
        }
        
        clientRepository.deleteById(id);
        logger.info("Client deleted with id: {}", id);
    }

    private ClientDto convertToDto(Client client) {
        return ClientDto.builder()
                .id(client.getId())
                .lib(client.getLib())
                .number(client.getNumber())
                .build();
    }
}

