package com.airline.service;

import com.airline.dto.PromotionDTOs;
import com.airline.entity.Promotion;
import com.airline.entity.enums.DiscountType;
import com.airline.repository.PromotionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class PromotionServiceTest28 {

    @Mock private PromotionRepository promotionRepository;
    @InjectMocks private PromotionService promotionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify coupon code validation for percentage discounts - Suite 28")
    void testValidatePercentageCoupon28() {
        Promotion promo = Promotion.builder()
            .id((long) 28)
            .couponCode("SKYNOVA28")
            .description("Special Promo 28")
            .discountType(DiscountType.PERCENTAGE)
            .discountValue(BigDecimal.valueOf(10.00))
            .minimumBookingAmount(BigDecimal.valueOf(100.00))
            .startDate(ZonedDateTime.now().minusDays(10))
            .endDate(ZonedDateTime.now().plusDays(30))
            .isActive(true)
            .build();

        when(promotionRepository.findByCouponCode("SKYNOVA28")).thenReturn(Optional.of(promo));

        var resp = promotionService.validateCoupon("SKYNOVA28", BigDecimal.valueOf(500.00));
        assertNotNull(resp);
        assertTrue(resp.getIsValid());
        assertEquals(BigDecimal.valueOf(50.00), resp.getCalculatedDiscountAmount());
    }
}
