package com.airline.entity;

import com.airline.entity.enums.CabinClassType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

@Entity
@Table(name = "fare_rules", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"flight_id", "cabin_class"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FareRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @Enumerated(EnumType.STRING)
    @Column(name = "cabin_class", nullable = false, length = 50)
    private CabinClassType cabinClass;

    @Column(name = "price_multiplier", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal priceMultiplier = BigDecimal.valueOf(1.00);

    @Column(name = "available_seats", nullable = false)
    private Integer availableSeats;

    @Column(name = "cancellation_fee_hours_48", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal cancellationFeeHours48 = BigDecimal.valueOf(50.00);

    @Column(name = "cancellation_fee_hours_24", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal cancellationFeeHours24 = BigDecimal.valueOf(100.00);

    @Column(name = "cancellation_fee_hours_under24", nullable = false, precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal cancellationFeeHoursUnder24 = BigDecimal.valueOf(200.00);

    @Column(name = "created_at", insertable = false, updatable = false)
    private ZonedDateTime createdAt;
}
