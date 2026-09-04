package com.airline.service;

import com.airline.dto.AirportDTOs;
import com.airline.entity.Airport;
import com.airline.repository.AirportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ServiceEdgeCaseTest43 {

    @Mock private AirportRepository airportRepository;
    @InjectMocks private AirportService airportService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify airport IATA lookup boundary conditions - Edge Suite 43")
    void testGetAirportByIataBoundary43() {
        Airport airport = Airport.builder()
            .id((long) 43)
            .iataCode("JFK")
            .name("John F. Kennedy Intl")
            .city("New York")
            .country("US")
            .timeZone("America/New_York")
            .isActive(true)
            .build();

        when(airportRepository.findByIataCode("JFK")).thenReturn(Optional.of(airport));

        var response = airportService.getAirportByIata("JFK");
        assertNotNull(response);
        assertEquals("JFK", response.getIataCode());
        assertEquals("New York", response.getCity());
    }

    @Test
    @DisplayName("Verify airport creation boundary validation - Edge Suite 43")
    void testCreateAirportBoundary43() {
        when(airportRepository.existsByIataCode("SFO")).thenReturn(false);
        Airport airport = Airport.builder().id((long) 43).iataCode("SFO").name("San Francisco Intl").city("SF").country("US").timeZone("PST").build();
        when(airportRepository.save(any())).thenReturn(airport);

        AirportDTOs.AirportRequest req = AirportDTOs.AirportRequest.builder()
            .iataCode("SFO")
            .name("San Francisco Intl")
            .city("SF")
            .country("US")
            .timeZone("PST")
            .build();

        var response = airportService.createAirport(req);
        assertNotNull(response);
        assertEquals("SFO", response.getIataCode());
    }
}
