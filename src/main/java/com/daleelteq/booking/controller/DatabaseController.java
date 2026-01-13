package com.daleelteq.booking.controller;

import com.daleelteq.booking.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DatabaseController {

    private final ServiceService serviceService;
    private final EmployeeService employeeService;
    private final ClientService clientService;
    private final EmployeeXServiceService esService;
    private final RendezVousService rendezVousService;
    private final NotificationService notificationService;

    /**
     * Clear entire database (delete all rows from all tables)
     * Requires confirmation via request body
     */
    @DeleteMapping("/clear-db")
    public ResponseEntity<Map<String, String>> clearDatabase(@RequestBody Map<String, Boolean> request) {
        Boolean confirm = request.getOrDefault("confirm", false);

        if (!confirm) {
            throw new IllegalArgumentException(
                    "Database clear requires 'confirm': true in request body"
            );
        }

        log.warn("CLEARING ENTIRE DATABASE - User confirmed");

        try {
            // Delete in order of foreign key dependencies (reverse order of creation)
            notificationService.deleteAllNotifications();
            rendezVousService.deleteAllRendezVous();
            esService.deleteAllES();
            clientService.deleteAllClients();
            employeeService.deleteAllEmployees();
            serviceService.deleteAllServices();

            log.info("Database cleared successfully");
            return ResponseEntity.ok(Map.of(
                    "status", "success",
                    "message", "All database tables have been cleared successfully"
            ));
        } catch (Exception e) {
            log.error("Error clearing database: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to clear database: " + e.getMessage());
        }
    }

    /**
     * Get database status (count of records in each table)
     */
    @GetMapping("/db-status")
    public ResponseEntity<Map<String, Object>> getDatabaseStatus() {
        return ResponseEntity.ok(Map.of(
                "services", serviceService.getAllServices().size(),
                "employees", employeeService.getAllEmployees().size(),
                "clients", clientService.getAllClients().size(),
                "employeeXServices", esService.getAllES().size(),
                "rendezVous", rendezVousService.getAllRendezVous().size(),
                "notifications", notificationService.getAllNotifications().size()
        ));
    }
}
