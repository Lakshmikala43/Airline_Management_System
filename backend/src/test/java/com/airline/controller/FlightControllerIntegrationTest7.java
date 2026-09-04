package com.airline.controller;

import com.airline.dto.FlightDTOs;
import com.airline.service.FlightService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FlightControllerIntegrationTest7 {

    @Mock private FlightService flightService;
    @InjectMocks private FlightController flightController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify search flights endpoint response - Integration Suite 7")
    void testSearchFlightsEndpoint7() {
        FlightDTOs.FlightResponse resp = FlightDTOs.FlightResponse.builder()
            .id((long) i)
            .flightNumber("SN-207")
            .originAirportCode("JFK")
            .destinationAirportCode("LHR")
            .basePrice(BigDecimal.valueOf(550.00))
            .calculatedFare(BigDecimal.valueOf(616.00))
            .build();

        when(flightService.searchFlights(any())).thenReturn(List.of(resp));

        FlightDTOs.FlightSearchRequest req = FlightDTOs.FlightSearchRequest.builder()
            .originAirportIata("JFK")
            .destinationAirportIata("LHR")
            .departureDate(java.time.LocalDate.now())
            .build();

        ResponseEntity<List<FlightDTOs.FlightResponse>> response = flightController.searchFlights(req);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("SN-207", response.getBody().get(0).getFlightNumber());
    }
}
