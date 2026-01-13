package com.daleelteq.booking.dto;

import lombok.*;

/**
 * Universal request DTO for endpoints that accept JSON body instead of path id.
 * Fields are strings for dates/times to allow flexible parsing by controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IdRequestDto {
    private Long id;
    private Long idE;
    private Long idS;
    private Boolean x2;
    private String start; // HH:mm
    private String end;   // HH:mm
    private String date;  // YYYY-MM-DD
    private String status;
}
