package com.daleelteq.booking.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

/**
 * Notification Entity (N)
 * Audit trail for booking/cancellation events.
 * Stores snapshots of x_2 and time_value at the time of notification.
 */
@Entity
@Table(name = "es_notification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = {"createdAt"})
@ToString(exclude = {"createdAt"})
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_r", nullable = false)
    private Long idR;

    @Column(nullable = false, length = 20)
    private String type;

    @Column(nullable = false, length = 50)
    private String value;

    @Column(nullable = false)
    private Boolean x2;

    @Column(nullable = false)
    private Integer timeValue;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
