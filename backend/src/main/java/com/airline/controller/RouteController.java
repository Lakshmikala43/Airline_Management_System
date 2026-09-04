package com.airline.controller;

import com.airline.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping
    public ResponseEntity<List<RouteService.RouteResponse>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<RouteService.RouteResponse> createRoute(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam Double distanceKm,
            @RequestParam Integer durationMinutes) {
        return ResponseEntity.ok(routeService.createRoute(origin, destination, distanceKm, durationMinutes));
    }
}
