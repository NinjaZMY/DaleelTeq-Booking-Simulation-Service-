package com.daleelteq.booking.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceDto {
    private Long id;
    private String lib;
    private Integer timeValue;
    private Instant createdAt;
}
