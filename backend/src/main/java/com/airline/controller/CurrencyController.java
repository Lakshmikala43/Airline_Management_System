package com.airline.controller;

import com.airline.service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/currency")
@RequiredArgsConstructor
public class CurrencyController {

    private final CurrencyExchangeService currencyService;

    @GetMapping("/convert")
    public ResponseEntity<CurrencyExchangeService.ConversionResponse> convertCurrency(
            @RequestParam BigDecimal amount,
            @RequestParam(defaultValue = "USD") String from,
            @RequestParam(defaultValue = "EUR") String to) {
        return ResponseEntity.ok(currencyService.convert(amount, from, to));
    }
}
