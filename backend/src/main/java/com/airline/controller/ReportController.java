package com.airline.controller;

import com.airline.dto.ReportDTOs;
import com.airline.service.ReportingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportingService reportingService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<ReportDTOs.AnalyticsDashboardResponse> getAnalytics() {
        return ResponseEntity.ok(reportingService.getDashboardAnalytics());
    }

    @GetMapping("/export/bookings")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<byte[]> exportBookingsCsv() {
        String csv = reportingService.generateBookingsCsvReport();
        byte[] bytes = csv.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=skynova_bookings_report.csv")
            .contentType(MediaType.parseMediaType("text/csv"))
            .body(bytes);
    }
}
