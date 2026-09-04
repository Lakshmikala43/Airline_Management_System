package com.airline.dto;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

public class ReportDTOs {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AnalyticsDashboardResponse {
        private Long totalFlights;
        private Long activeFlights;
        private Long totalBookings;
        private Long confirmedBookings;
        private Long cancelledBookings;
        private BigDecimal totalRevenue;
        private Long totalPassengers;
        private Double cancellationRatePercentage;
        private Double averageOccupancyPercentage;

        private List<TimeSeriesDataPoint> bookingTrend;
        private List<TimeSeriesDataPoint> revenueTrend;
        private List<CategoryDataPoint> popularRoutes;
        private List<CategoryDataPoint> cabinDistribution;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class TimeSeriesDataPoint {
        private String date;
        private BigDecimal value;
        private Long count;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CategoryDataPoint {
        private String category;
        private Long count;
        private BigDecimal value;
    }
}
