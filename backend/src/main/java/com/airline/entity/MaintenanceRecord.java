package com.airline.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "maintenance_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaintenanceRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    @Column(name = "maintenance_type", nullable = false, length = 50)
    private String maintenanceType; // A_CHECK, B_CHECK, C_CHECK, D_CHECK, ENGINE_INSPECTION

    @Column(nullable = false, length = 255)
    private String description;

    @Column(name = "technician_name", nullable = false, length = 100)
    private String technicianName;

    @Column(name = "cost_usd", nullable = false, precision = 10, scale = 2)
    private java.math.BigDecimal costUsd;

    @Column(name = "performed_at", nullable = false)
    private ZonedDateTime performedAt;

    @Column(name = "next_due_at", nullable = false)
    private ZonedDateTime nextDueAt;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String status = "COMPLETED";
}
