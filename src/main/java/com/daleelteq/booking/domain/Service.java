package com.daleelteq.booking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * S (Services) - Represents a service with base duration
 */
@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Service {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String lib;

    @Column(nullable = false)
    private Integer timeValue; // Base duration: 15, 20, 25, or 30 minutes

    @Version
    private Long version;
}

