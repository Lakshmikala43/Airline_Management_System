package com.airline.service;

import com.airline.entity.Booking;
import com.airline.entity.Flight;
import com.airline.entity.Route;
import com.airline.entity.Airport;
import com.airline.entity.User;
import com.airline.entity.Aircraft;
import com.airline.repository.BookingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BaggageTrackingServiceTest18 {

    @Mock private BookingRepository bookingRepository;
    @InjectMocks private BaggageTrackingService baggageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify live baggage tracking resolution and telemetry - Suite 18")
    void testTrackBaggageSuite18() {
        Airport origin = Airport.builder().iataCode("JFK").build();
        Airport dest = Airport.builder().iataCode("LHR").build();
        Route route = Route.builder().originAirport(origin).destinationAirport(dest).build();
        Aircraft aircraft = Aircraft.builder().registrationNumber("N701SN").build();
        Flight flight = Flight.builder().flightNumber("SN-101").route(route).aircraft(aircraft).build();
        User user = User.builder().firstName("John").lastName("Doe").build();

        Booking booking = Booking.builder()
            .id(1L)
            .pnr("K7P4M2")
            .user(user)
            .flight(flight)
            .build();

        when(bookingRepository.findByPnr("K7P4M2")).thenReturn(Optional.of(booking));

        var resp = baggageService.getBaggageStatus("BAG-K7P4M2-01");
        assertNotNull(resp);
        assertEquals("K7P4M2", resp.getPnr());
        assertEquals("SN-101", resp.getFlightNumber());
        assertEquals(3, resp.getScanHistory().size());
    }
}
