package com.airline.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "crew_members")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CrewMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false, unique = true, length = 20)
    private String employeeId;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, length = 50)
    private String role; // CAPTAIN, FIRST_OFFICER, PURSER, FLIGHT_ATTENDANT

    @Column(name = "license_number", length = 50)
    private String licenseNumber;

    @Column(name = "flight_hours_accumulated")
    @Builder.Default
    private Integer flightHoursAccumulated = 0;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
