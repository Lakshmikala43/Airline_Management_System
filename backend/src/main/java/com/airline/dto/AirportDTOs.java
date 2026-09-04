package com.airline.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

public class AirportDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AirportRequest {
        @NotBlank(message = "IATA code is required")
        @Size(min = 3, max = 3, message = "IATA code must be exactly 3 characters")
        private String iataCode;

        private String icaoCode;

        @NotBlank(message = "Airport name is required")
        private String name;

        @NotBlank(message = "City is required")
        private String city;

        @NotBlank(message = "Country is required")
        private String country;

        private Double latitude;
        private Double longitude;

        @NotBlank(message = "Time zone is required")
        private String timeZone;

        private String terminalInfo;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AirportResponse {
        private Long id;
        private String iataCode;
        private String icaoCode;
        private String name;
        private String city;
        private String country;
        private Double latitude;
        private Double longitude;
        private String timeZone;
        private String terminalInfo;
        private Boolean isActive;
    }
}
