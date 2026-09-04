package com.airline.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherDelaySimulationService {

    @Getter
    @AllArgsConstructor
    @Builder
    public static class WeatherReportResponse {
        private String airportIata;
        private String weatherCondition;
        private Double temperatureCelsius;
        private Double windSpeedKmh;
        private Integer visibilityMeters;
        private Integer delayRiskFactorPercentage;
        private String delayRecommendation;
    }

    public WeatherReportResponse getWeatherReport(String airportIata) {
        int risk = 15;
        String condition = "Clear Sky";
        if (airportIata.equalsIgnoreCase("LHR") || airportIata.equalsIgnoreCase("CDG")) {
            risk = 35;
            condition = "Light Fog & Rain";
        } else if (airportIata.equalsIgnoreCase("ORD") || airportIata.equalsIgnoreCase("JFK")) {
            risk = 25;
            condition = "Crosswinds";
        }

        return WeatherReportResponse.builder()
            .airportIata(airportIata.toUpperCase())
            .weatherCondition(condition)
            .temperatureCelsius(18.5)
            .windSpeedKmh(24.0)
            .visibilityMeters(9500)
            .delayRiskFactorPercentage(risk)
            .delayRecommendation(risk > 30 ? "Monitor departure runway queue" : "Optimal flight conditions")
            .build();
    }
}
