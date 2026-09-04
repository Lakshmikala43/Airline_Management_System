package com.airline.controller;

import com.airline.dto.PaymentDTOs;
import com.airline.service.MockPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final MockPaymentService paymentService;

    @PostMapping("/process")
    public ResponseEntity<PaymentDTOs.PaymentResponse> processPayment(@Valid @RequestBody PaymentDTOs.PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPayment(request));
    }
}
