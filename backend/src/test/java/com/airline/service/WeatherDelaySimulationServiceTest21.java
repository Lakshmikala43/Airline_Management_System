package com.airline.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherDelaySimulationServiceTest21 {

    private WeatherDelaySimulationService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherDelaySimulationService();
    }

    @Test
    @DisplayName("Verify airport weather forecast and delay risk evaluation - Suite 21")
    void testGetWeatherReportSuite21() {
        var resp = weatherService.getWeatherReport("JFK");
        assertNotNull(resp);
        assertEquals("JFK", resp.getAirportIata());
        assertTrue(resp.getDelayRiskFactorPercentage() >= 0);
    }
}
