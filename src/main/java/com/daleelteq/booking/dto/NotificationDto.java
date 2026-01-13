package com.daleelteq.booking.dto;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private Long id;
    private Long idR;
    private String type;
    private String value;
    private Boolean x2;
    private Integer timeValue;
    private Instant createdAt;
}
