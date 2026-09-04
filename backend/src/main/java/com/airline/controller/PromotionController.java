package com.airline.controller;

import com.airline.dto.PromotionDTOs;
import com.airline.service.PromotionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/promotions")
@RequiredArgsConstructor
public class PromotionController {

    private final PromotionService promotionService;

    @GetMapping("/validate")
    public ResponseEntity<PromotionDTOs.CouponValidationResponse> validateCoupon(
            @RequestParam String code,
            @RequestParam(required = false) BigDecimal amount) {
        return ResponseEntity.ok(promotionService.validateCoupon(code, amount));
    }

    @GetMapping
    public ResponseEntity<List<PromotionDTOs.PromotionResponse>> getAllPromotions() {
        return ResponseEntity.ok(promotionService.getAllPromotions());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN', 'ADMIN')")
    public ResponseEntity<PromotionDTOs.PromotionResponse> createPromotion(@Valid @RequestBody PromotionDTOs.PromotionRequest request) {
        return ResponseEntity.ok(promotionService.createPromotion(request));
    }
}
