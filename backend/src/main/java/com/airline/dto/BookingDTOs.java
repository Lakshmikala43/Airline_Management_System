package com.airline.dto;

import com.airline.entity.enums.BookingStatus;
import com.airline.entity.enums.CabinClassType;
import com.airline.entity.enums.TripType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

public class BookingDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookingCreateRequest {
        @NotNull(message = "Flight ID is required")
        private Long flightId;

        private Long returnFlightId;

        @Builder.Default
        private TripType tripType = TripType.ONE_WAY;

        @NotNull(message = "Cabin class is required")
        private CabinClassType cabinClass;

        @NotEmpty(message = "At least one passenger is required")
        @Valid
        private List<PassengerDTOs.PassengerRequest> passengers;

        private String couponCode;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BookingResponse {
        private Long id;
        private String pnr;
        private String customerEmail;
        private String customerName;
        private FlightDTOs.FlightResponse flight;
        private FlightDTOs.FlightResponse returnFlight;
        private TripType tripType;
        private CabinClassType cabinClass;
        private Integer passengerCount;
        private BigDecimal baseFare;
        private BigDecimal taxAmount;
        private BigDecimal seatFee;
        private BigDecimal baggageFee;
        private BigDecimal discountAmount;
        private BigDecimal totalAmount;
        private BookingStatus status;
        private ZonedDateTime bookingDate;
        private List<PassengerDTOs.PassengerResponse> passengers;
        private String paymentStatus;
        private String ticketNumber;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RescheduleRequest {
        @NotNull(message = "New flight ID is required")
        private Long newFlightId;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CancellationResponse {
        private String pnr;
        private BigDecimal originalAmount;
        private BigDecimal cancellationFee;
        private BigDecimal refundAmount;
        private String status;
        private String refundReference;
    }
}
