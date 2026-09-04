package com.airline;

import com.airline.entity.BookingSeat;
import com.airline.exception.SeatUnavailableException;
import com.airline.repository.AircraftSeatRepository;
import com.airline.repository.BookingSeatRepository;
import com.airline.repository.FlightRepository;
import com.airline.service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SeatServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private AircraftSeatRepository aircraftSeatRepository;

    @Mock
    private BookingSeatRepository bookingSeatRepository;

    @InjectMocks
    private SeatService seatService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Validate seat availability fails when seat is already occupied")
    void testSeatConflictThrowsException() {
        when(bookingSeatRepository.existsByFlightIdAndSeatId(1L, 10L)).thenReturn(true);

        assertThrows(SeatUnavailableException.class, () -> {
            seatService.validateSeatAvailabilityForLock(1L, 10L);
        });
    }

    @Test
    @DisplayName("Validate seat availability passes when seat is unreserved")
    void testSeatAvailablePasses() {
        when(bookingSeatRepository.existsByFlightIdAndSeatId(1L, 10L)).thenReturn(false);
        when(bookingSeatRepository.findForLockingByFlightIdAndSeatId(1L, 10L)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> {
            seatService.validateSeatAvailabilityForLock(1L, 10L);
        });
    }
}
