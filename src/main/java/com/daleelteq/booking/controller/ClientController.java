package com.daleelteq.booking.controller;

import com.daleelteq.booking.dto.ClientDto;
import com.daleelteq.booking.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ui/clients")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @GetMapping
    public ResponseEntity<List<ClientDto>> getAllClients() {
        return ResponseEntity.ok(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDto> getClientById(@PathVariable Long id) {
        return ResponseEntity.ok(clientService.getClientById(id));
    }

    @PostMapping
    public ResponseEntity<ClientDto> createClient(@RequestBody ClientDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(clientService.createClient(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientDto> updateClient(@PathVariable Long id, @RequestBody ClientDto dto) {
        return ResponseEntity.ok(clientService.updateClient(id, dto));
    }

    @PutMapping("/entity")
    public ResponseEntity<ClientDto> updateClientByEntity(@RequestBody ClientDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Missing 'id' in request body for update. Available client ids: " +
                    clientService.getAllClients().stream()
                            .map(c -> String.valueOf(c.getId()))
                            .toList()
            );
        }
        return ResponseEntity.ok(clientService.updateClient(dto.getId(), dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/entity")
    public ResponseEntity<Void> deleteClientByEntity(@RequestBody ClientDto dto) {
        if (dto.getId() == null) {
            throw new IllegalArgumentException(
                    "Missing 'id' in request body for delete. Available client ids: " +
                    clientService.getAllClients().stream()
                            .map(c -> String.valueOf(c.getId()))
                            .toList()
            );
        }
        clientService.deleteClient(dto.getId());
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/clear")
    public ResponseEntity<Void> deleteAllClients() {
        clientService.deleteAllClients();
        return ResponseEntity.noContent().build();
    }
}
