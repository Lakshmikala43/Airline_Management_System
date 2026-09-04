package com.airline.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyExchangeService {

    private static final Map<String, BigDecimal> RATES = Map.of(
        "USD", BigDecimal.ONE,
        "EUR", BigDecimal.valueOf(0.92),
        "GBP", BigDecimal.valueOf(0.79),
        "INR", BigDecimal.valueOf(83.15),
        "JPY", BigDecimal.valueOf(151.40),
        "AED", BigDecimal.valueOf(3.67),
        "SGD", BigDecimal.valueOf(1.35),
        "AUD", BigDecimal.valueOf(1.52)
    );

    @Getter
    @AllArgsConstructor
    @Builder
    public static class ConversionResponse {
        private String fromCurrency;
        private String toCurrency;
        private BigDecimal originalAmount;
        private BigDecimal exchangeRate;
        private BigDecimal convertedAmount;
    }

    public ConversionResponse convert(BigDecimal amount, String fromCurrency, String toCurrency) {
        BigDecimal fromRate = RATES.getOrDefault(fromCurrency.toUpperCase(), BigDecimal.ONE);
        BigDecimal toRate = RATES.getOrDefault(toCurrency.toUpperCase(), BigDecimal.ONE);

        BigDecimal amountInUsd = amount.divide(fromRate, 6, RoundingMode.HALF_UP);
        BigDecimal converted = amountInUsd.multiply(toRate).setScale(2, RoundingMode.HALF_UP);
        BigDecimal effectiveRate = toRate.divide(fromRate, 4, RoundingMode.HALF_UP);

        return ConversionResponse.builder()
            .fromCurrency(fromCurrency.toUpperCase())
            .toCurrency(toCurrency.toUpperCase())
            .originalAmount(amount)
            .exchangeRate(effectiveRate)
            .convertedAmount(converted)
            .build();
    }
}
