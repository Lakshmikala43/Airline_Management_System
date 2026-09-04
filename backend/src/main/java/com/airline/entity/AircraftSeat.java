package com.airline.entity;

import com.airline.entity.enums.CabinClassType;
import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "aircraft_seats", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"aircraft_id", "seat_number"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AircraftSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_id", nullable = false)
    private Aircraft aircraft;

    @Column(name = "seat_number", nullable = false, length = 10)
    private String seatNumber;

    @Column(name = "seat_row", nullable = false)
    private Integer seatRow;

    @Column(name = "seat_column", nullable = false, length = 5)
    private String seatColumn;

    @Enumerated(EnumType.STRING)
    @Column(name = "cabin_class", nullable = false, length = 50)
    private CabinClassType cabinClass;

    @Column(name = "is_window", nullable = false)
    @Builder.Default
    private Boolean isWindow = false;

    @Column(name = "is_aisle", nullable = false)
    @Builder.Default
    private Boolean isAisle = false;

    @Column(name = "is_exit_row", nullable = false)
    @Builder.Default
    private Boolean isExitRow = false;

    @Column(name = "extra_legroom", nullable = false)
    @Builder.Default
    private Boolean extraLegroom = false;

    @Column(name = "is_blocked", nullable = false)
    @Builder.Default
    private Boolean isBlocked = false;

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
