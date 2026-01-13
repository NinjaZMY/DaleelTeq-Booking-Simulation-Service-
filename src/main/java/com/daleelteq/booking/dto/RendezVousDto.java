package com.daleelteq.booking.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RendezVousDto {
    private Long id;
    private Long idES;
    private Long idC;
    private String status;
    private Instant createdAt;
    private Instant updatedAt;
}
