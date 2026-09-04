package com.airline.controller;

import com.airline.dto.FlightDTOs;
import com.airline.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/search")
    public ResponseEntity<List<FlightDTOs.FlightResponse>> searchFlights(@Valid @RequestBody FlightDTOs.FlightSearchRequest request) {
        return ResponseEntity.ok(flightService.searchFlights(request));
    }

    @GetMapping
    public ResponseEntity<List<FlightDTOs.FlightResponse>> getAllFlights() {
        return ResponseEntity.ok(flightService.getAllFlights());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightDTOs.FlightResponse> getFlightById(@PathVariable Long id) {
        return ResponseEntity.ok(flightService.getFlightById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<FlightDTOs.FlightResponse> createFlight(@Valid @RequestBody FlightDTOs.FlightRequest request) {
        return ResponseEntity.ok(flightService.createFlight(request));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'FLIGHT_MANAGER')")
    public ResponseEntity<FlightDTOs.FlightResponse> updateFlightStatus(
            @PathVariable Long id,
            @Valid @RequestBody FlightDTOs.FlightStatusUpdateRequest request) {
        return ResponseEntity.ok(flightService.updateFlightStatus(id, request));
    }
}
