package com.airline.controller;

import com.airline.service.AircraftMaintenanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/maintenance")
@RequiredArgsConstructor
public class MaintenanceController {

    private final AircraftMaintenanceService maintenanceService;

    @GetMapping("/aircraft/{aircraftId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<List<AircraftMaintenanceService.MaintenanceResponse>> getAircraftLogs(@PathVariable Long aircraftId) {
        return ResponseEntity.ok(maintenanceService.getRecordsForAircraft(aircraftId));
    }

    @PostMapping("/aircraft/{aircraftId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<AircraftMaintenanceService.MaintenanceResponse> logMaintenance(
            @PathVariable Long aircraftId,
            @RequestParam String type,
            @RequestParam String description,
            @RequestParam String technician,
            @RequestParam BigDecimal cost) {
        return ResponseEntity.ok(maintenanceService.logMaintenance(aircraftId, type, description, technician, cost));
    }
}
