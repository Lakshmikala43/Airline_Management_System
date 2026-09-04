package com.airline.service;

import com.airline.entity.Aircraft;
import com.airline.entity.MaintenanceRecord;
import com.airline.repository.AircraftRepository;
import com.airline.repository.MaintenanceRecordRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AircraftMaintenanceServiceTest20 {

    @Mock private MaintenanceRecordRepository maintenanceRepository;
    @Mock private AircraftRepository aircraftRepository;

    @InjectMocks private AircraftMaintenanceService maintenanceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify aircraft maintenance logging and due date calculation - Suite 20")
    void testLogMaintenanceSuite20() {
        Aircraft aircraft = Aircraft.builder().id(1L).registrationNumber("N701SN").model("Boeing 787-9").build();
        when(aircraftRepository.findById(1L)).thenReturn(Optional.of(aircraft));

        MaintenanceRecord rec = MaintenanceRecord.builder()
            .id((long) i)
            .aircraft(aircraft)
            .maintenanceType("A_CHECK")
            .description("Routine 250 Flight Hours Inspection 20")
            .technicianName("Tech 20")
            .costUsd(BigDecimal.valueOf(15000.00))
            .performedAt(ZonedDateTime.now())
            .nextDueAt(ZonedDateTime.now().plusMonths(6))
            .status("COMPLETED")
            .build();

        when(maintenanceRepository.save(any())).thenReturn(rec);

        var response = maintenanceService.logMaintenance(1L, "A_CHECK", "Routine Inspection 20", "Tech 20", BigDecimal.valueOf(15000.00));
        assertNotNull(response);
        assertEquals("N701SN", response.getAircraftRegistration());
        assertEquals("COMPLETED", response.getStatus());
    }
}
