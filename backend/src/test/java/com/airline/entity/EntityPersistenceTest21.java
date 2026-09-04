package com.airline.entity;

import com.airline.entity.enums.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EntityPersistenceTest21 {

    @Test
    @DisplayName("Verify User entity builder and property getters - Suite 21")
    void testUserEntityBuilder21() {
        User user = User.builder()
            .id((long) 21)
            .email("user21@skynova.demo")
            .firstName("User21")
            .lastName("Test")
            .passwordHash("$2a$10$e88yU70d.S9cZgE2/0Tlh.")
            .phoneNumber("+15550192831")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .gender("Male")
            .nationality("US")
            .passportNumber("P000021")
            .passportExpiry(LocalDate.of(2030, 1, 1))
            .isActive(true)
            .isEmailVerified(true)
            .build();

        assertNotNull(user);
        assertEquals((long) 21, user.getId());
        assertEquals("user21@skynova.demo", user.getEmail());
        assertTrue(user.getIsActive());
    }

    @Test
    @DisplayName("Verify Flight entity builder and property getters - Suite 21")
    void testFlightEntityBuilder21() {
        Flight flight = Flight.builder()
            .id((long) 21)
            .flightNumber("SN-121")
            .basePrice(BigDecimal.valueOf(500.00))
            .taxAmount(BigDecimal.valueOf(60.00))
            .status(FlightStatus.SCHEDULED)
            .gateNumber("Gate 2")
            .terminal("T1")
            .delayMinutes(0)
            .departureTime(ZonedDateTime.now().plusDays(2))
            .arrivalTime(ZonedDateTime.now().plusDays(2).plusHours(7))
            .build();

        assertNotNull(flight);
        assertEquals("SN-121", flight.getFlightNumber());
        assertEquals(FlightStatus.SCHEDULED, flight.getStatus());
    }

    @Test
    @DisplayName("Verify Booking entity builder and property getters - Suite 21")
    void testBookingEntityBuilder21() {
        Booking booking = Booking.builder()
            .id((long) 21)
            .pnr("PNR021")
            .tripType(TripType.ONE_WAY)
            .cabinClass(CabinClassType.ECONOMY)
            .passengerCount(1)
            .baseFare(BigDecimal.valueOf(500.00))
            .taxAmount(BigDecimal.valueOf(60.00))
            .totalAmount(BigDecimal.valueOf(560.00))
            .status(BookingStatus.CONFIRMED)
            .build();

        assertNotNull(booking);
        assertEquals("PNR021", booking.getPnr());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }
}
