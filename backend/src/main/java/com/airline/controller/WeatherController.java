package com.airline.controller;

import com.airline.service.WeatherDelaySimulationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherDelaySimulationService weatherService;

    @GetMapping("/airport/{iataCode}")
    public ResponseEntity<WeatherDelaySimulationService.WeatherReportResponse> getWeather(@PathVariable String iataCode) {
        return ResponseEntity.ok(weatherService.getWeatherReport(iataCode));
    }
}
