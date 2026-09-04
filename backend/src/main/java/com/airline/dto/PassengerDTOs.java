package com.airline.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;

public class PassengerDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PassengerRequest {
        private String title;

        @NotBlank(message = "First name is required")
        private String firstName;

        private String middleName;

        @NotBlank(message = "Last name is required")
        private String lastName;

        @NotNull(message = "Date of birth is required")
        private LocalDate dateOfBirth;

        @NotBlank(message = "Gender is required")
        private String gender;

        @NotBlank(message = "Nationality is required")
        private String nationality;

        @NotBlank(message = "Passport number is required")
        private String passportNumber;

        @NotNull(message = "Passport expiry date is required")
        private LocalDate passportExpiry;

        @Email(message = "Invalid email format")
        private String email;

        private String phoneNumber;
        private Long selectedSeatId;
        private String specialRequest;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PassengerResponse {
        private Long id;
        private String title;
        private String firstName;
        private String lastName;
        private LocalDate dateOfBirth;
        private String gender;
        private String nationality;
        private String passportNumber;
        private LocalDate passportExpiry;
        private String email;
        private String phoneNumber;
        private String seatNumber;
    }
}
