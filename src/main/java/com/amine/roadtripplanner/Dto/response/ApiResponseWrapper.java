package com.amine.roadtripplanner.Dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
public class ApiResponseWrapper<T> {
    private boolean success;
    private String message;
    private T data;
    private Integer count;
    private LocalDateTime timestamp;
    private ErrorDetails error;

    public static <T> ApiResponseWrapper<T> success(T data, String message) {
        return ApiResponseWrapper.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .count(data instanceof Collection ? ((Collection<?>) data).size() : null)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponseWrapper<T> error(String message, String code, String details) {
        return ApiResponseWrapper.<T>builder()
                .success(false)
                .message(message)
                .error(ErrorDetails.builder()
                        .code(code)
                        .details(details)
                        .build())
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Data
    @Builder
    public static class ErrorDetails {
        private String code;
        private String details;
    }
}
