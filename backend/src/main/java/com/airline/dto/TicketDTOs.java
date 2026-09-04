package com.airline.dto;

import lombok.*;
import java.time.ZonedDateTime;

public class TicketDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TicketResponse {
        private String ticketNumber;
        private String pnr;
        private String passengerName;
        private String passportNumber;
        private String flightNumber;
        private String airlineName;
        private String originAirport;
        private String destinationAirport;
        private ZonedDateTime departureTime;
        private ZonedDateTime arrivalTime;
        private String seatNumber;
        private String cabinClass;
        private String gateNumber;
        private String terminal;
        private String qrCodeData;
        private String pdfDownloadUrl;
        private ZonedDateTime issueDate;
        private String status;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TicketVerificationResponse {
        private Boolean isValid;
        private String ticketNumber;
        private String pnr;
        private String passengerName;
        private String flightNumber;
        private String originAirport;
        private String destinationAirport;
        private ZonedDateTime departureTime;
        private String status;
        private String verificationMessage;
    }
}
