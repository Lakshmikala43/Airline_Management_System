package com.airline.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ErrorResponse {
        private Integer status;
        private String error;
        private String message;
        private ZonedDateTime timestamp;
        private Map<String, String> validationErrors;
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.NOT_FOUND.value())
            .error("NOT_FOUND")
            .message(ex.getMessage())
            .timestamp(ZonedDateTime.now())
            .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({SeatUnavailableException.class, DoubleBookingException.class})
    public ResponseEntity<ErrorResponse> handleSeatConflict(RuntimeException ex) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.CONFLICT.value())
            .error("SEAT_CONFLICT")
            .message(ex.getMessage())
            .timestamp(ZonedDateTime.now())
            .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler({InvalidBookingStateException.class, InvalidPromotionException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(RuntimeException ex) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("BAD_REQUEST")
            .message(ex.getMessage())
            .timestamp(ZonedDateTime.now())
            .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentProcessingException.class)
    public ResponseEntity<ErrorResponse> handlePaymentError(PaymentProcessingException ex) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.PAYMENT_REQUIRED.value())
            .error("PAYMENT_FAILED")
            .message(ex.getMessage())
            .timestamp(ZonedDateTime.now())
            .build();
        return new ResponseEntity<>(response, HttpStatus.PAYMENT_REQUIRED);
    }

    @ExceptionHandler(UnauthorizedOperationException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorized(UnauthorizedOperationException ex) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.FORBIDDEN.value())
            .error("FORBIDDEN")
            .message(ex.getMessage())
            .timestamp(ZonedDateTime.now())
            .build();
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.BAD_REQUEST.value())
            .error("VALIDATION_FAILED")
            .message("Input validation failed for one or more fields")
            .timestamp(ZonedDateTime.now())
            .validationErrors(errors)
            .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ErrorResponse response = ErrorResponse.builder()
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .error("INTERNAL_SERVER_ERROR")
            .message("An unexpected error occurred: " + ex.getMessage())
            .timestamp(ZonedDateTime.now())
            .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
