package com.airline.entity;

import com.airline.entity.enums.CabinClassType;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cabin_classes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CabinClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private CabinClassType code;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "base_fare_multiplier", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal baseFareMultiplier = BigDecimal.valueOf(1.00);

    @Column(name = "baggage_allowance_kg", nullable = false)
    @Builder.Default
    private Integer baggageAllowanceKg = 20;

    @Column(name = "cabin_baggage_allowance_kg", nullable = false)
    @Builder.Default
    private Integer cabinBaggageAllowanceKg = 7;

    @Column(name = "is_refundable_by_default", nullable = false)
    @Builder.Default
    private Boolean isRefundableByDefault = true;

    @Column(name = "change_fee_percentage", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal changeFeePercentage = BigDecimal.valueOf(10.00);

    @Column(length = 255)
    private String description;
}
