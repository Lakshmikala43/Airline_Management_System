package com.airline.service;

import com.airline.entity.Aircraft;
import com.airline.entity.MaintenanceRecord;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.AircraftRepository;
import com.airline.repository.MaintenanceRecordRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AircraftMaintenanceService {

    private final MaintenanceRecordRepository maintenanceRepository;
    private final AircraftRepository aircraftRepository;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class MaintenanceResponse {
        private Long id;
        private String aircraftRegistration;
        private String aircraftModel;
        private String maintenanceType;
        private String description;
        private String technicianName;
        private BigDecimal costUsd;
        private ZonedDateTime performedAt;
        private ZonedDateTime nextDueAt;
        private String status;
    }

    @Transactional(readOnly = true)
    public List<MaintenanceResponse> getRecordsForAircraft(Long aircraftId) {
        return maintenanceRepository.findByAircraftId(aircraftId).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public MaintenanceResponse logMaintenance(Long aircraftId, String type, String desc, String tech, BigDecimal cost) {
        Aircraft aircraft = aircraftRepository.findById(aircraftId)
            .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found: " + aircraftId));

        MaintenanceRecord rec = MaintenanceRecord.builder()
            .aircraft(aircraft)
            .maintenanceType(type)
            .description(desc)
            .technicianName(tech)
            .costUsd(cost)
            .performedAt(ZonedDateTime.now())
            .nextDueAt(ZonedDateTime.now().plusMonths(6))
            .status("COMPLETED")
            .build();

        return mapToResponse(maintenanceRepository.save(rec));
    }

    private MaintenanceResponse mapToResponse(MaintenanceRecord rec) {
        return MaintenanceResponse.builder()
            .id(rec.getId())
            .aircraftRegistration(rec.getAircraft().getRegistrationNumber())
            .aircraftModel(rec.getAircraft().getModel())
            .maintenanceType(rec.getMaintenanceType())
            .description(rec.getDescription())
            .technicianName(rec.getTechnicianName())
            .costUsd(rec.getCostUsd())
            .performedAt(rec.getPerformedAt())
            .nextDueAt(rec.getNextDueAt())
            .status(rec.getStatus())
            .build();
    }
}
