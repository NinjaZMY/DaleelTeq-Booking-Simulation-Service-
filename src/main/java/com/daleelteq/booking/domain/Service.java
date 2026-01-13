package com.daleelteq.booking.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Service Entity (S)
 * Defines available appointment types with base duration.
 * Time values allowed: 15, 20, 25, 30 minutes.
 */
@Entity
@Table(name = "services")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt"})
@ToString(exclude = {"createdAt"})
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    private String lib;

    @Column(nullable = false)
    private Integer timeValue;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
