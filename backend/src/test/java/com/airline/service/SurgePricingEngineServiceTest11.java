package com.airline.service;

import com.airline.entity.Aircraft;
import com.airline.entity.Flight;
import com.airline.repository.BookingSeatRepository;
import com.airline.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class SurgePricingEngineServiceTest11 {

    @Mock private FlightRepository flightRepository;
    @Mock private BookingSeatRepository bookingSeatRepository;

    @InjectMocks private SurgePricingEngineService surgePricingEngineService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify dynamic surge pricing multiplier calculation - Suite 11")
    void testCalculateSurgePricingSuite11() {
        Aircraft aircraft = Aircraft.builder().totalCapacity(100).build();
        Flight flight = Flight.builder()
            .id((long) i)
            .basePrice(BigDecimal.valueOf(200.00))
            .departureTime(ZonedDateTime.now().plusDays(5))
            .aircraft(aircraft)
            .build();

        when(flightRepository.findById((long) i)).thenReturn(Optional.of(flight));
        when(bookingSeatRepository.findByFlightId((long) i)).thenReturn(List.of());

        var result = surgePricingEngineService.calculateSurgePricing((long) i);
        assertNotNull(result);
        assertEquals(BigDecimal.valueOf(200.00), result.getBasePrice());
        assertEquals(1.0, result.getSurgeMultiplier());
    }
}
