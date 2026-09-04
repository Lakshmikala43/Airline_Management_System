package com.airline.controller;

import com.airline.service.BaggageTrackingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/baggage")
@RequiredArgsConstructor
public class BaggageController {

    private final BaggageTrackingService baggageService;

    @GetMapping("/track/{tagNumber}")
    public ResponseEntity<BaggageTrackingService.BaggageTagResponse> trackBaggage(@PathVariable String tagNumber) {
        return ResponseEntity.ok(baggageService.getBaggageStatus(tagNumber));
    }
}
