package com.daleelteq.booking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * N (ES_Notification) - Represents a notification event
 * Created when a rendez_vous is booked or cancelled
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_r", nullable = false)
    private RendezVous rendezVous;

    @Column(nullable = false, length = 100)
    private String type; // "booked", "cancelled"

    @Column(nullable = false, columnDefinition = "TEXT")
    private String value; // Inherits rendez_vous status

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Version
    private Long version;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

