package com.airline.dto;

import com.airline.entity.enums.CabinClassType;
import com.airline.entity.enums.FlightStatus;
import com.airline.entity.enums.TripType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public class FlightDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FlightSearchRequest {
        @NotBlank(message = "Origin airport is required")
        private String originAirportIata;

        @NotBlank(message = "Destination airport is required")
        private String destinationAirportIata;

        @NotNull(message = "Departure date is required")
        private LocalDate departureDate;

        private LocalDate returnDate;

        @Builder.Default
        private TripType tripType = TripType.ONE_WAY;

        @NotNull(message = "Passengers count is required")
        @Min(value = 1, message = "At least 1 passenger is required")
        @Builder.Default
        private Integer passengerCount = 1;

        @Builder.Default
        private CabinClassType cabinClass = CabinClassType.ECONOMY;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FlightResponse {
        private Long id;
        private String flightNumber;
        private String airlineName;
        private String airlineCode;
        private String originAirportCode;
        private String originAirportName;
        private String originCity;
        private String destinationAirportCode;
        private String destinationAirportName;
        private String destinationCity;
        private String aircraftModel;
        private ZonedDateTime departureTime;
        private ZonedDateTime arrivalTime;
        private Integer durationMinutes;
        private BigDecimal basePrice;
        private BigDecimal calculatedFare;
        private BigDecimal taxAmount;
        private FlightStatus status;
        private String gateNumber;
        private String terminal;
        private Integer availableSeats;
        private Integer delayMinutes;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FlightRequest {
        @NotBlank(message = "Flight number is required")
        private String flightNumber;

        @NotNull(message = "Airline ID is required")
        private Long airlineId;

        @NotNull(message = "Route ID is required")
        private Long routeId;

        @NotNull(message = "Aircraft ID is required")
        private Long aircraftId;

        @NotNull(message = "Departure time is required")
        private ZonedDateTime departureTime;

        @NotNull(message = "Arrival time is required")
        private ZonedDateTime arrivalTime;

        @NotNull(message = "Base price is required")
        private BigDecimal basePrice;

        private BigDecimal taxAmount;
        private String gateNumber;
        private String terminal;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FlightStatusUpdateRequest {
        @NotNull(message = "Flight status is required")
        private FlightStatus status;
        private String gateNumber;
        private String terminal;
        private Integer delayMinutes;
        private String cancellationReason;
    }
}
