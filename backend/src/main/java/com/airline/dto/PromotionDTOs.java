package com.airline.dto;

import com.airline.entity.enums.DiscountType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class PromotionDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PromotionRequest {
        @NotBlank(message = "Coupon code is required")
        private String couponCode;

        @NotBlank(message = "Description is required")
        private String description;

        @NotNull(message = "Discount type is required")
        private DiscountType discountType;

        @NotNull(message = "Discount value is required")
        private BigDecimal discountValue;

        private BigDecimal minimumBookingAmount;
        private BigDecimal maximumDiscountAmount;

        @NotNull(message = "Start date is required")
        private ZonedDateTime startDate;

        @NotNull(message = "End date is required")
        private ZonedDateTime endDate;

        private Integer usageLimit;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PromotionResponse {
        private Long id;
        private String couponCode;
        private String description;
        private DiscountType discountType;
        private BigDecimal discountValue;
        private BigDecimal minimumBookingAmount;
        private BigDecimal maximumDiscountAmount;
        private ZonedDateTime startDate;
        private ZonedDateTime endDate;
        private Integer usageLimit;
        private Integer timesUsed;
        private Boolean isActive;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CouponValidationResponse {
        private Boolean isValid;
        private String couponCode;
        private DiscountType discountType;
        private BigDecimal discountValue;
        private BigDecimal calculatedDiscountAmount;
        private String message;
    }
}
