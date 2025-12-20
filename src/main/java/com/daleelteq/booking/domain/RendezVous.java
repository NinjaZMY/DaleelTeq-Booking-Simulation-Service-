package com.daleelteq.booking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * R (Rendez_vous) - Represents a booking/reservation
 * A client books a specific employee service timeslot
 */
@Entity
@Table(name = "rendez_vous")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVous {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_es", nullable = false)
    private EmployeeService employeeService;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_c", nullable = false)
    private Client client;

    @Column(nullable = false, length = 100)
    private String status; // "Active", "Cancelled by Client", "Cancelled by Employee"

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = true)
    private LocalDateTime cancelledAt;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

