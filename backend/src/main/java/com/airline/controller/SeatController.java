package com.airline.controller;

import com.airline.dto.AircraftDTOs;
import com.airline.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flights")
@RequiredArgsConstructor
public class SeatController {

    private final SeatService seatService;

    @GetMapping("/{flightId}/seats")
    public ResponseEntity<AircraftDTOs.SeatMapResponse> getSeatMap(@PathVariable Long flightId) {
        return ResponseEntity.ok(seatService.getSeatMapForFlight(flightId));
    }
}
