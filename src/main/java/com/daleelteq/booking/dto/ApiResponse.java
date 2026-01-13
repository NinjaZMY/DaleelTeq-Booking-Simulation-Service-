package com.daleelteq.booking.dto;

import lombok.*;

import java.util.List;

/**
 * Generic API response wrapper used by REST controllers.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiResponse<T> {
    private boolean success;
    private T data;
    private String message;
    private String error;
    private List<?> details;

    public static <T> ApiResponse<T> success(T data, String message) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String errorMessage, String message) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(errorMessage)
                .message(message)
                .build();
    }

    public static <T> ApiResponse<T> error(String errorMessage, String message, List<?> details) {
        return ApiResponse.<T>builder()
                .success(false)
                .error(errorMessage)
                .message(message)
                .details(details)
                .build();
    }
}

