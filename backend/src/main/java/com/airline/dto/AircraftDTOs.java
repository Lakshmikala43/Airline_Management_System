package com.airline.dto;

import com.airline.entity.enums.AircraftStatus;
import com.airline.entity.enums.CabinClassType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.List;

public class AircraftDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AircraftRequest {
        @NotBlank(message = "Registration number is required")
        private String registrationNumber;

        @NotNull(message = "Airline ID is required")
        private Long airlineId;

        @NotBlank(message = "Manufacturer is required")
        private String manufacturer;

        @NotBlank(message = "Model is required")
        private String model;

        @Min(value = 1, message = "Total capacity must be greater than 0")
        private Integer totalCapacity;

        private Integer economyCapacity;
        private Integer premiumEconomyCapacity;
        private Integer businessCapacity;
        private Integer firstClassCapacity;
        private Integer manufactureYear;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AircraftResponse {
        private Long id;
        private String registrationNumber;
        private String airlineName;
        private String manufacturer;
        private String model;
        private Integer totalCapacity;
        private Integer economyCapacity;
        private Integer premiumEconomyCapacity;
        private Integer businessCapacity;
        private Integer firstClassCapacity;
        private AircraftStatus status;
        private Integer manufactureYear;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SeatDTO {
        private Long id;
        private String seatNumber;
        private Integer seatRow;
        private String seatColumn;
        private CabinClassType cabinClass;
        private Boolean isWindow;
        private Boolean isAisle;
        private Boolean isExitRow;
        private Boolean extraLegroom;
        private Boolean isOccupied;
        private Boolean isBlocked;
        private Double seatSurcharge;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SeatMapResponse {
        private Long flightId;
        private String flightNumber;
        private String aircraftModel;
        private List<SeatDTO> seats;
    }
}
