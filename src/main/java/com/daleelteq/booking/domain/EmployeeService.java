package com.daleelteq.booking.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * ES (Employee_x_Services) - Represents a timeslot/appointment slot
 * An employee provides a service during a specific time slot on a specific date
 */
@Entity
@Table(name = "employee_services")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeService {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_e", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_s", nullable = false)
    private Service service;

    @Column(nullable = false)
    private Boolean x2; // 0=Single duration, 1=Double duration (true/false)

    @Column(nullable = false)
    private LocalTime start; // HH:mm format

    @Column(nullable = false)
    private LocalTime end; // HH:mm format

    @Column(nullable = true)
    private LocalDate date; // YYYY-MM-DD format

    @Column(nullable = false, length = 50)
    private String status; // "free" or "taken"

    @Column(nullable = false)
    private Integer timeValue; // Effective time value: service.timeValue * (x2 ? 2 : 1)

    @Version
    private Long version;
}

