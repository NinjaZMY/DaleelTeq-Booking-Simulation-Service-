package com.daleelteq.booking.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {
    private Long id;
    private String lib;
    private String number;
    private Instant createdAt;
}
