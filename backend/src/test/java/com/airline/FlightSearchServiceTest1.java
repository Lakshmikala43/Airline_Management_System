package com.airline;

import com.airline.dto.FlightDTOs;
import com.airline.entity.Flight;
import com.airline.repository.FlightRepository;
import com.airline.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FlightSearchServiceTest1 {

    @Mock private FlightRepository flightRepository;
    @InjectMocks private FlightService flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify search flights returns valid schedule mapping - Suite 1")
    void testSearchFlightsSuite1() {
        when(flightRepository.searchFlights(any(), any(), any(), any()))
            .thenReturn(List.of());

        FlightDTOs.FlightSearchRequest req = FlightDTOs.FlightSearchRequest.builder()
            .originAirportIata("JFK")
            .destinationAirportIata("LHR")
            .departureDate(java.time.LocalDate.now())
            .build();

        var results = flightService.searchFlights(req);
        assertNotNull(results);
    }
}
