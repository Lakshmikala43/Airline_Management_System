package com.airline.service;

import com.airline.entity.Booking;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.BookingRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BaggageTrackingService {

    private final BookingRepository bookingRepository;

    public enum BaggageStatus {
        CHECKED_IN, SECURITY_CLEARED, LOADED_ON_AIRCRAFT, IN_TRANSIT, ARRIVED_AT_CAROUSEL, CLAIMED
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BaggageTagResponse {
        private String tagNumber;
        private String pnr;
        private String passengerName;
        private String flightNumber;
        private Integer weightKg;
        private BaggageStatus currentStatus;
        private String currentCarousel;
        private List<BaggageScanEvent> scanHistory;
    }

    @Getter
    @AllArgsConstructor
    @Builder
    public static class BaggageScanEvent {
        private String location;
        private BaggageStatus status;
        private ZonedDateTime timestamp;
        private String scannerId;
    }

    @Transactional(readOnly = true)
    public BaggageTagResponse getBaggageStatus(String tagNumber) {
        // Tag format: BAG-PNR-INDEX
        String[] parts = tagNumber.split("-");
        String pnr = parts.length > 1 ? parts[1] : "K7P4M2";

        Booking booking = bookingRepository.findByPnr(pnr)
            .orElseThrow(() -> new ResourceNotFoundException("Baggage tag not found: " + tagNumber));

        List<BaggageScanEvent> history = new ArrayList<>();
        history.add(BaggageScanEvent.builder()
            .location(booking.getFlight().getRoute().getOriginAirport().getIataCode() + " Terminal Check-in")
            .status(BaggageStatus.CHECKED_IN)
            .timestamp(ZonedDateTime.now().minusHours(3))
            .scannerId("SCAN-019")
            .build());

        history.add(BaggageScanEvent.builder()
            .location(booking.getFlight().getRoute().getOriginAirport().getIataCode() + " Security Screening")
            .status(BaggageStatus.SECURITY_CLEARED)
            .timestamp(ZonedDateTime.now().minusHours(2))
            .scannerId("SCAN-044")
            .build());

        history.add(BaggageScanEvent.builder()
            .location("Aircraft Cargo Hold (" + booking.getFlight().getAircraft().getRegistrationNumber() + ")")
            .status(BaggageStatus.LOADED_ON_AIRCRAFT)
            .timestamp(ZonedDateTime.now().minusHours(1))
            .scannerId("SCAN-088")
            .build());

        return BaggageTagResponse.builder()
            .tagNumber(tagNumber)
            .pnr(booking.getPnr())
            .passengerName(booking.getUser().getFirstName() + " " + booking.getUser().getLastName())
            .flightNumber(booking.getFlight().getFlightNumber())
            .weightKg(22)
            .currentStatus(BaggageStatus.LOADED_ON_AIRCRAFT)
            .currentCarousel("Carousel 4B")
            .scanHistory(history)
            .build();
    }
}
