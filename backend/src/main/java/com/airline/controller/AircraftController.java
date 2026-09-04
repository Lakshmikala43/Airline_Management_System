package com.airline.controller;

import com.airline.dto.AircraftDTOs;
import com.airline.service.AircraftService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/aircraft")
@RequiredArgsConstructor
public class AircraftController {

    private final AircraftService aircraftService;

    @GetMapping
    public ResponseEntity<List<AircraftDTOs.AircraftResponse>> getAllAircraft() {
        return ResponseEntity.ok(aircraftService.getAllAircraft());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AircraftDTOs.AircraftResponse> getAircraftById(@PathVariable Long id) {
        return ResponseEntity.ok(aircraftService.getAircraftById(id));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<AircraftDTOs.AircraftResponse> createAircraft(@Valid @RequestBody AircraftDTOs.AircraftRequest request) {
        return ResponseEntity.ok(aircraftService.createAircraft(request));
    }
}
