package com.daleelteq.booking.dto;

import lombok.*;

import java.time.Instant;

/**
 * Placeholder DTO from previous generation. The ES endpoints use EmployeeXServiceDto.
 * This DTO is kept empty to avoid breaking references; if you want to restore
 * a specialized EmployeeServiceDto, replace this content with fields you need.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeServiceDto {
    private Long id; // kept minimal to avoid compilation errors
    private String lib;
    private Integer timeValue;
    private Instant createdAt;
}

