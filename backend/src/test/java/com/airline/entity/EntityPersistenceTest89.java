package com.airline.entity;

import com.airline.entity.enums.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.*;

class EntityPersistenceTest89 {

    @Test
    @DisplayName("Verify User entity builder and property getters - Suite 89")
    void testUserEntityBuilder89() {
        User user = User.builder()
            .id((long) 89)
            .email("user89@skynova.demo")
            .firstName("User89")
            .lastName("Test")
            .passwordHash("$2a$10$e88yU70d.S9cZgE2/0Tlh.")
            .phoneNumber("+15550192839")
            .dateOfBirth(LocalDate.of(1990, 1, 1))
            .gender("Male")
            .nationality("US")
            .passportNumber("P000089")
            .passportExpiry(LocalDate.of(2030, 1, 1))
            .isActive(true)
            .isEmailVerified(true)
            .build();

        assertNotNull(user);
        assertEquals((long) 89, user.getId());
        assertEquals("user89@skynova.demo", user.getEmail());
        assertTrue(user.getIsActive());
    }

    @Test
    @DisplayName("Verify Flight entity builder and property getters - Suite 89")
    void testFlightEntityBuilder89() {
        Flight flight = Flight.builder()
            .id((long) 89)
            .flightNumber("SN-189")
            .basePrice(BigDecimal.valueOf(500.00))
            .taxAmount(BigDecimal.valueOf(60.00))
            .status(FlightStatus.SCHEDULED)
            .gateNumber("Gate 10")
            .terminal("T1")
            .delayMinutes(0)
            .departureTime(ZonedDateTime.now().plusDays(2))
            .arrivalTime(ZonedDateTime.now().plusDays(2).plusHours(7))
            .build();

        assertNotNull(flight);
        assertEquals("SN-189", flight.getFlightNumber());
        assertEquals(FlightStatus.SCHEDULED, flight.getStatus());
    }

    @Test
    @DisplayName("Verify Booking entity builder and property getters - Suite 89")
    void testBookingEntityBuilder89() {
        Booking booking = Booking.builder()
            .id((long) 89)
            .pnr("PNR089")
            .tripType(TripType.ONE_WAY)
            .cabinClass(CabinClassType.ECONOMY)
            .passengerCount(1)
            .baseFare(BigDecimal.valueOf(500.00))
            .taxAmount(BigDecimal.valueOf(60.00))
            .totalAmount(BigDecimal.valueOf(560.00))
            .status(BookingStatus.CONFIRMED)
            .build();

        assertNotNull(booking);
        assertEquals("PNR089", booking.getPnr());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }
}
