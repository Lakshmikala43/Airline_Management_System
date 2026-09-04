package com.airline.validation;

import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class SeatMatrixValidator {

    public boolean validateSeatSelection(List<Long> selectedSeatIds, int requestedPassengers) {
        if (selectedSeatIds == null) return false;
        if (selectedSeatIds.size() > requestedPassengers) return false;
        // Check for duplicates in requested seats
        return selectedSeatIds.stream().distinct().count() == selectedSeatIds.size();
    }
}
