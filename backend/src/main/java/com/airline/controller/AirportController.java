package com.airline.controller;

import com.airline.dto.AirportDTOs;
import com.airline.service.AirportService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/airports")
@RequiredArgsConstructor
public class AirportController {

    private final AirportService airportService;

    @GetMapping
    public ResponseEntity<List<AirportDTOs.AirportResponse>> getAllAirports() {
        return ResponseEntity.ok(airportService.getAllAirports());
    }

    @GetMapping("/search")
    public ResponseEntity<List<AirportDTOs.AirportResponse>> searchAirports(@RequestParam(required = false) String query) {
        return ResponseEntity.ok(airportService.searchAirports(query));
    }

    @GetMapping("/{iataCode}")
    public ResponseEntity<AirportDTOs.AirportResponse> getAirportByIata(@PathVariable String iataCode) {
        return ResponseEntity.ok(airportService.getAirportByIata(iataCode));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<AirportDTOs.AirportResponse> createAirport(@Valid @RequestBody AirportDTOs.AirportRequest request) {
        return ResponseEntity.ok(airportService.createAirport(request));
    }
}
