package com.airline.dto;

import com.airline.entity.enums.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PaymentDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentRequest {
        @NotNull(message = "Booking ID is required")
        private Long bookingId;

        @NotBlank(message = "Payment method is required")
        private String paymentMethod; // e.g. CREDIT_CARD, MOCK_CARD, UPI

        private String cardHolderName;
        private String cardNumber;
        private String expiryDate;
        private String cvv; // Demo only - NOT stored or logged
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PaymentResponse {
        private String transactionReference;
        private Long bookingId;
        private String pnr;
        private BigDecimal amount;
        private String currency;
        private String paymentMethod;
        private String authorizationCode;
        private String maskedCardNumber;
        private PaymentStatus status;
        private ZonedDateTime createdAt;
        private String message;
    }
}
