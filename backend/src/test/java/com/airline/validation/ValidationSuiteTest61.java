package com.airline.validation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidationSuiteTest61 {

    private PassportValidator passportValidator;
    private FlightScheduleValidator scheduleValidator;
    private SeatMatrixValidator seatValidator;

    @BeforeEach
    void setUp() {
        passportValidator = new PassportValidator();
        scheduleValidator = new FlightScheduleValidator();
        seatValidator = new SeatMatrixValidator();
    }

    @Test
    @DisplayName("Verify passport validation logic - Suite 61")
    void testPassportValidation61() {
        assertTrue(passportValidator.isValidPassportNumber("A12345678"));
        assertFalse(passportValidator.isValidPassportNumber("short"));
        assertTrue(passportValidator.isPassportValidForTravel(LocalDate.now().plusYears(2), LocalDate.now()));
    }

    @Test
    @DisplayName("Verify flight schedule time boundary validation - Suite 61")
    void testScheduleValidation61() {
        ZonedDateTime dep = ZonedDateTime.now().plusDays(1);
        ZonedDateTime arr = dep.plusHours(5);
        assertTrue(scheduleValidator.isValidFlightSchedule(dep, arr));
        assertFalse(scheduleValidator.isValidFlightSchedule(arr, dep));
    }

    @Test
    @DisplayName("Verify seat matrix selection boundaries - Suite 61")
    void testSeatMatrixValidation61() {
        assertTrue(seatValidator.validateSeatSelection(List.of(1L, 2L), 2));
        assertFalse(seatValidator.validateSeatSelection(List.of(1L, 1L), 2));
    }
}
