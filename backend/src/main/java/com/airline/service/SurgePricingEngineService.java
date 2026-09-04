package com.airline.service;

import com.airline.entity.Flight;
import com.airline.repository.BookingSeatRepository;
import com.airline.repository.FlightRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class SurgePricingEngineService {

    private final FlightRepository flightRepository;
    private final BookingSeatRepository bookingSeatRepository;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class SurgeCalculationResult {
        private Long flightId;
        private BigDecimal basePrice;
        private Double occupancyRatePercentage;
        private Double surgeMultiplier;
        private BigDecimal adjustedBasePrice;
        private String surgeReason;
    }

    @Transactional(readOnly = true)
    public SurgeCalculationResult calculateSurgePricing(Long flightId) {
        Flight flight = flightRepository.findById(flightId).orElseThrow();
        int totalCap = flight.getAircraft().getTotalCapacity();
        long reservedCount = bookingSeatRepository.findByFlightId(flightId).size();

        double occupancy = totalCap > 0 ? ((double) reservedCount / totalCap) * 100.0 : 0.0;
        double multiplier = 1.0;
        String reason = "Standard Rate";

        // Surge pricing logic based on seat occupancy and proximity to departure
        if (occupancy > 85.0) {
            multiplier = 1.45;
            reason = "High Demand (>85% Occupancy)";
        } else if (occupancy > 70.0) {
            multiplier = 1.25;
            reason = "Moderate Demand (>70% Occupancy)";
        } else if (occupancy > 50.0) {
            multiplier = 1.10;
            reason = "Steady Demand (>50% Occupancy)";
        }

        long hoursUntilDeparture = Duration.between(ZonedDateTime.now(), flight.getDepartureTime()).toHours();
        if (hoursUntilDeparture < 24 && hoursUntilDeparture > 0) {
            multiplier += 0.20;
            reason += " + Last Minute Surge (<24h to Departure)";
        }

        BigDecimal adjusted = flight.getBasePrice().multiply(BigDecimal.valueOf(multiplier)).setScale(2, RoundingMode.HALF_UP);

        return SurgeCalculationResult.builder()
            .flightId(flight.getId())
            .basePrice(flight.getBasePrice())
            .occupancyRatePercentage(Math.round(occupancy * 100.0) / 100.0)
            .surgeMultiplier(Math.round(multiplier * 100.0) / 100.0)
            .adjustedBasePrice(adjusted)
            .surgeReason(reason)
            .build();
    }
}
