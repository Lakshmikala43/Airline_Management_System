package com.airline.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WeatherDelaySimulationServiceTest14 {

    private WeatherDelaySimulationService weatherService;

    @BeforeEach
    void setUp() {
        weatherService = new WeatherDelaySimulationService();
    }

    @Test
    @DisplayName("Verify airport weather forecast and delay risk evaluation - Suite 14")
    void testGetWeatherReportSuite14() {
        var resp = weatherService.getWeatherReport("JFK");
        assertNotNull(resp);
        assertEquals("JFK", resp.getAirportIata());
        assertTrue(resp.getDelayRiskFactorPercentage() >= 0);
    }
}
