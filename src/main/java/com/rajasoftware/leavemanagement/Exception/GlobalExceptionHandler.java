package com.rajasoftware.leavemanagement.Exception;

import java.time.Clock;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import com.rajasoftware.leavemanagement.DTO.ApiError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final Clock clock;

    public GlobalExceptionHandler(Clock clock) {
        this.clock = clock;
    }

    @ExceptionHandler(LeaveNotFoundException.class)
    public ResponseEntity<ApiError> handleLeaveNotFound(LeaveNotFoundException exception) {
        return response(HttpStatus.NOT_FOUND, exception.getMessage(), Map.of());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException exception) {
        Map<String, String> validationErrors = new LinkedHashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                validationErrors.putIfAbsent(error.getField(), error.getDefaultMessage()));
        exception.getBindingResult().getGlobalErrors().forEach(error ->
                validationErrors.putIfAbsent("request", error.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST, "Request validation failed", validationErrors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleUnreadableRequest(HttpMessageNotReadableException exception) {
        return response(HttpStatus.BAD_REQUEST, "Request body is missing or malformed", Map.of());
    }

    private ResponseEntity<ApiError> response(
            HttpStatus status,
            String message,
            Map<String, String> validationErrors) {
        ApiError body = new ApiError(
                Instant.now(clock),
                status.value(),
                status.getReasonPhrase(),
                message,
                validationErrors);
        return ResponseEntity.status(status).body(body);
    }
}
