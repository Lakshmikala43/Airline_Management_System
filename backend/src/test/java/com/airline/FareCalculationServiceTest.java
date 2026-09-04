package com.airline;

import com.airline.entity.Flight;
import com.airline.entity.enums.CabinClassType;
import com.airline.repository.CabinClassRepository;
import com.airline.service.FareCalculationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class FareCalculationServiceTest {

    @Mock
    private CabinClassRepository cabinClassRepository;

    @InjectMocks
    private FareCalculationService fareCalculationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(cabinClassRepository.findByCode(any())).thenReturn(Optional.empty());
    }

    @Test
    @DisplayName("Calculate Economy fare for 1 passenger without discount")
    void testEconomyFareCalculation() {
        Flight flight = Flight.builder()
            .basePrice(BigDecimal.valueOf(500.00))
            .build();

        var breakdown = fareCalculationService.calculateFare(
            flight,
            CabinClassType.ECONOMY,
            1,
            BigDecimal.ZERO,
            BigDecimal.ZERO
        );

        assertNotNull(breakdown);
        assertEquals(BigDecimal.valueOf(500.00), breakdown.getTotalBaseFare());
        assertEquals(BigDecimal.valueOf(60.00), breakdown.getTaxAmount()); // 12% tax
        assertEquals(BigDecimal.valueOf(560.00), breakdown.getFinalTotalAmount());
    }

    @Test
    @DisplayName("Calculate Business Class fare with multiplier (2.20x) and coupon discount")
    void testBusinessClassFareCalculationWithDiscount() {
        Flight flight = Flight.builder()
            .basePrice(BigDecimal.valueOf(1000.00))
            .build();

        var breakdown = fareCalculationService.calculateFare(
            flight,
            CabinClassType.BUSINESS,
            2,
            BigDecimal.valueOf(50.00), // seat fee
            BigDecimal.valueOf(100.00) // discount
        );

        // 1000 * 2.20 = 2200 per pax -> 4400 total base
        assertEquals(BigDecimal.valueOf(4400.00), breakdown.getTotalBaseFare());
        // Tax 12% of 4400 = 528.00
        assertEquals(BigDecimal.valueOf(528.00), breakdown.getTaxAmount());
        // 4400 + 528 + 50 - 100 = 4878.00
        assertEquals(BigDecimal.valueOf(4878.00), breakdown.getFinalTotalAmount());
    }
}
