package com.airline.service;

import com.airline.entity.CabinClass;
import com.airline.entity.Flight;
import com.airline.entity.enums.CabinClassType;
import com.airline.repository.CabinClassRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class FareCalculationService {

    private final CabinClassRepository cabinClassRepository;

    @Getter
    @Builder
    public static class FareBreakdown {
        private BigDecimal baseFare;
        private BigDecimal cabinMultiplier;
        private BigDecimal perPassengerPrice;
        private BigDecimal totalBaseFare;
        private BigDecimal taxAmount;
        private BigDecimal seatFee;
        private BigDecimal baggageFee;
        private BigDecimal discountAmount;
        private BigDecimal finalTotalAmount;
    }

    public FareBreakdown calculateFare(Flight flight, CabinClassType cabinClass, Integer passengerCount, BigDecimal seatFee, BigDecimal discountAmount) {
        BigDecimal basePrice = flight.getBasePrice();

        BigDecimal multiplier = BigDecimal.valueOf(1.00);
        CabinClass cabinClassEntity = cabinClassRepository.findByCode(cabinClass).orElse(null);
        if (cabinClassEntity != null && cabinClassEntity.getBaseFareMultiplier() != null) {
            multiplier = cabinClassEntity.getBaseFareMultiplier();
        } else {
            switch (cabinClass) {
                case PREMIUM_ECONOMY -> multiplier = BigDecimal.valueOf(1.35);
                case BUSINESS -> multiplier = BigDecimal.valueOf(2.20);
                case FIRST_CLASS -> multiplier = BigDecimal.valueOf(3.80);
                default -> multiplier = BigDecimal.valueOf(1.00);
            }
        }

        BigDecimal perPaxPrice = basePrice.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalBase = perPaxPrice.multiply(BigDecimal.valueOf(passengerCount)).setScale(2, RoundingMode.HALF_UP);

        // Calculate 12% airport tax
        BigDecimal tax = totalBase.multiply(BigDecimal.valueOf(0.12)).setScale(2, RoundingMode.HALF_UP);

        BigDecimal seatsTotal = seatFee != null ? seatFee : BigDecimal.ZERO;
        BigDecimal discount = discountAmount != null ? discountAmount : BigDecimal.ZERO;

        BigDecimal total = totalBase.add(tax).add(seatsTotal).subtract(discount);
        if (total.compareTo(BigDecimal.ZERO) < 0) {
            total = BigDecimal.ZERO;
        }

        return FareBreakdown.builder()
            .baseFare(basePrice)
            .cabinMultiplier(multiplier)
            .perPassengerPrice(perPaxPrice)
            .totalBaseFare(totalBase)
            .taxAmount(tax)
            .seatFee(seatsTotal)
            .baggageFee(BigDecimal.ZERO)
            .discountAmount(discount)
            .finalTotalAmount(total)
            .build();
    }
}
