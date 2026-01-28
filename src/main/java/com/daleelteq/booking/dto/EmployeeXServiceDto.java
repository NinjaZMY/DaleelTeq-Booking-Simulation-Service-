package com.daleelteq.booking.dto;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeXServiceDto {
    private Long id;
    private Long idE;
    private Long idS;
    private Boolean x2;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer timeValue;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
