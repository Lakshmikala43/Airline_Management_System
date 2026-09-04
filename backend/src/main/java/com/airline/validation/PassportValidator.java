package com.airline.validation;

import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.regex.Pattern;

@Component
public class PassportValidator {

    private static final Pattern PASSPORT_PATTERN = Pattern.compile("^[A-Z0-9]{6,12}$");

    public boolean isValidPassportNumber(String passportNumber) {
        if (passportNumber == null || passportNumber.isBlank()) return false;
        return PASSPORT_PATTERN.matcher(passportNumber.trim().toUpperCase()).matches();
    }

    public boolean isPassportValidForTravel(LocalDate expiryDate, LocalDate travelDate) {
        if (expiryDate == null || travelDate == null) return false;
        // Passport must be valid for at least 6 months after travel date
        return expiryDate.isAfter(travelDate.plusMonths(6));
    }
}
