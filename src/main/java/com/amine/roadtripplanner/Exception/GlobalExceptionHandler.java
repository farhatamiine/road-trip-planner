package com.amine.roadtripplanner.Exception;

import com.amine.roadtripplanner.Dto.response.ApiResponseWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "Authentication Error",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(UserNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                "User Not found",
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseWrapper<?>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String errorMessage = ex.getMessage();

        // Check for date parsing issues
        if (errorMessage.contains("LocalDate") && errorMessage.contains("DateTimeParseException")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseWrapper.error(
                            "Invalid date format or value",
                            "INVALID_DATE",
                            "Please provide a valid date in yyyy-MM-dd format"));
        }

        // Generic JSON parsing error
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseWrapper.error(
                        "Invalid request format",
                        "INVALID_REQUEST_FORMAT",
                        "Please check your JSON syntax and data types"));
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiResponseWrapper<?>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        String errorCode = "RESOURCE_NOT_FOUND";

        // If we have resource type information, make the error code more specific
        if (ex.getResourceType() != null) {
            errorCode = ex.getResourceType().toUpperCase() + "_NOT_FOUND";
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponseWrapper.error(
                        "Resource not found",
                        errorCode,
                        ex.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponseWrapper<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponseWrapper.error(
                        "Validation failed",
                        "VALIDATION_ERROR",
                        "Please check the following fields: " + String.join(", ", errors.keySet())));


    }

    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
    }

}
