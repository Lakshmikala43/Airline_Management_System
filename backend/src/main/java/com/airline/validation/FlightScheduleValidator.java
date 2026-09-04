package com.airline.validation;

import org.springframework.stereotype.Component;
import java.time.ZonedDateTime;

@Component
public class FlightScheduleValidator {

    public boolean isValidFlightSchedule(ZonedDateTime departureTime, ZonedDateTime arrivalTime) {
        if (departureTime == null || arrivalTime == null) return false;
        if (departureTime.isBefore(ZonedDateTime.now())) return false;
        return arrivalTime.isAfter(departureTime.plusMinutes(30)); // Minimum flight time 30 mins
    }
}
