package com.airline.controller;

import com.airline.service.CrewRosterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/crew")
@RequiredArgsConstructor
public class CrewController {

    private final CrewRosterService crewService;

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN', 'FLIGHT_MANAGER')")
    public ResponseEntity<List<CrewRosterService.CrewResponse>> getCrewRoster() {
        return ResponseEntity.ok(crewService.getAllCrew());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<CrewRosterService.CrewResponse> addCrew(
            @RequestParam String employeeId,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String role,
            @RequestParam(required = false) String license) {
        return ResponseEntity.ok(crewService.addCrewMember(employeeId, firstName, lastName, role, license));
    }
}
