package com.airline.entity;

import com.airline.entity.enums.AircraftStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aircraft")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Aircraft {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false, unique = true, length = 20)
    private String registrationNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;

    @Column(nullable = false, length = 100)
    private String manufacturer;

    @Column(nullable = false, length = 100)
    private String model;

    @Column(name = "total_capacity", nullable = false)
    private Integer totalCapacity;

    @Column(name = "economy_capacity", nullable = false)
    @Builder.Default
    private Integer economyCapacity = 0;

    @Column(name = "premium_economy_capacity", nullable = false)
    @Builder.Default
    private Integer premiumEconomyCapacity = 0;

    @Column(name = "business_capacity", nullable = false)
    @Builder.Default
    private Integer businessCapacity = 0;

    @Column(name = "first_class_capacity", nullable = false)
    @Builder.Default
    private Integer firstClassCapacity = 0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    @Builder.Default
    private AircraftStatus status = AircraftStatus.ACTIVE;

    @Column(name = "manufacture_year")
    private Integer manufactureYear;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @OneToMany(mappedBy = "aircraft", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<AircraftSeat> seats = new ArrayList<>();
}
