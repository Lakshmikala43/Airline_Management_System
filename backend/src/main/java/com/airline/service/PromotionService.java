package com.airline.service;

import com.airline.dto.PromotionDTOs;
import com.airline.entity.Promotion;
import com.airline.entity.enums.DiscountType;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.PromotionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PromotionService {

    private final PromotionRepository promotionRepository;

    @Transactional(readOnly = true)
    public PromotionDTOs.CouponValidationResponse validateCoupon(String couponCode, BigDecimal bookingAmount) {
        Promotion promo = promotionRepository.findByCouponCode(couponCode.toUpperCase())
            .orElse(null);

        if (promo == null || !promo.getIsActive()) {
            return PromotionDTOs.CouponValidationResponse.builder()
                .isValid(false)
                .couponCode(couponCode)
                .message("Invalid or inactive coupon code")
                .build();
        }

        ZonedDateTime now = ZonedDateTime.now();
        if (now.isBefore(promo.getStartDate()) || now.isAfter(promo.getEndDate())) {
            return PromotionDTOs.CouponValidationResponse.builder()
                .isValid(false)
                .couponCode(couponCode)
                .message("Coupon code has expired or is not yet active")
                .build();
        }

        if (bookingAmount != null && bookingAmount.compareTo(promo.getMinimumBookingAmount()) < 0) {
            return PromotionDTOs.CouponValidationResponse.builder()
                .isValid(false)
                .couponCode(couponCode)
                .message("Minimum booking amount of $" + promo.getMinimumBookingAmount() + " required to apply this coupon")
                .build();
        }

        BigDecimal discount = BigDecimal.ZERO;
        if (promo.getDiscountType() == DiscountType.PERCENTAGE) {
            discount = bookingAmount.multiply(promo.getDiscountValue()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
            if (promo.getMaximumDiscountAmount() != null && discount.compareTo(promo.getMaximumDiscountAmount()) > 0) {
                discount = promo.getMaximumDiscountAmount();
            }
        } else {
            discount = promo.getDiscountValue();
        }

        return PromotionDTOs.CouponValidationResponse.builder()
            .isValid(true)
            .couponCode(promo.getCouponCode())
            .discountType(promo.getDiscountType())
            .discountValue(promo.getDiscountValue())
            .calculatedDiscountAmount(discount)
            .message("Coupon code applied successfully!")
            .build();
    }

    @Transactional(readOnly = true)
    public List<PromotionDTOs.PromotionResponse> getAllPromotions() {
        return promotionRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public PromotionDTOs.PromotionResponse createPromotion(PromotionDTOs.PromotionRequest request) {
        Promotion promo = Promotion.builder()
            .couponCode(request.getCouponCode().toUpperCase())
            .description(request.getDescription())
            .discountType(request.getDiscountType())
            .discountValue(request.getDiscountValue())
            .minimumBookingAmount(request.getMinimumBookingAmount() != null ? request.getMinimumBookingAmount() : BigDecimal.ZERO)
            .maximumDiscountAmount(request.getMaximumDiscountAmount())
            .startDate(request.getStartDate())
            .endDate(request.getEndDate())
            .usageLimit(request.getUsageLimit() != null ? request.getUsageLimit() : 1000)
            .timesUsed(0)
            .isActive(true)
            .build();

        return mapToResponse(promotionRepository.save(promo));
    }

    private PromotionDTOs.PromotionResponse mapToResponse(Promotion promo) {
        return PromotionDTOs.PromotionResponse.builder()
            .id(promo.getId())
            .couponCode(promo.getCouponCode())
            .description(promo.getDescription())
            .discountType(promo.getDiscountType())
            .discountValue(promo.getDiscountValue())
            .minimumBookingAmount(promo.getMinimumBookingAmount())
            .maximumDiscountAmount(promo.getMaximumDiscountAmount())
            .startDate(promo.getStartDate())
            .endDate(promo.getEndDate())
            .usageLimit(promo.getUsageLimit())
            .timesUsed(promo.getTimesUsed())
            .isActive(promo.getIsActive())
            .build();
    }
}
