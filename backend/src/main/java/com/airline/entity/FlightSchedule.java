package com.airline.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;

@Entity
@Table(name = "flight_schedules")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FlightSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Column(name = "operating_days", nullable = false, length = 50)
    private String operatingDays; // e.g. MON,WED,FRI

    @Column(name = "departure_time_local", nullable = false)
    private LocalTime departureTimeLocal;

    @Column(name = "arrival_time_local", nullable = false)
    private LocalTime arrivalTimeLocal;

    @Column(name = "effective_start_date", nullable = false)
    private LocalDate effectiveStartDate;

    @Column(name = "effective_end_date", nullable = false)
    private LocalDate effectiveEndDate;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
