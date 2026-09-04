package com.airline.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CurrencyExchangeServiceTest20 {

    private CurrencyExchangeService currencyService;

    @BeforeEach
    void setUp() {
        currencyService = new CurrencyExchangeService();
    }

    @Test
    @DisplayName("Verify USD to EUR conversion calculation - Suite 20")
    void testUsdToEurConversion20() {
        var resp = currencyService.convert(BigDecimal.valueOf(100.00), "USD", "EUR");
        assertNotNull(resp);
        assertEquals("USD", resp.getFromCurrency());
        assertEquals("EUR", resp.getToCurrency());
        assertEquals(BigDecimal.valueOf(92.00), resp.getConvertedAmount());
    }

    @Test
    @DisplayName("Verify USD to INR conversion calculation - Suite 20")
    void testUsdToInrConversion20() {
        var resp = currencyService.convert(BigDecimal.valueOf(100.00), "USD", "INR");
        assertNotNull(resp);
        assertEquals("INR", resp.getToCurrency());
        assertEquals(BigDecimal.valueOf(8315.00), resp.getConvertedAmount());
    }
}
