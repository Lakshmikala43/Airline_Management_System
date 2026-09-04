package com.airline.service;

import com.airline.dto.BookingDTOs;
import com.airline.entity.Booking;
import com.airline.entity.Flight;
import com.airline.entity.User;
import com.airline.entity.enums.BookingStatus;
import com.airline.entity.enums.CabinClassType;
import com.airline.entity.enums.TripType;
import com.airline.repository.BookingRepository;
import com.airline.repository.FlightRepository;
import com.airline.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BookingEngineServiceTest16 {

    @Mock private BookingRepository bookingRepository;
    @Mock private FlightRepository flightRepository;
    @Mock private UserRepository userRepository;
    @Mock private FareCalculationService fareCalculationService;
    @Mock private SeatService seatService;
    @Mock private PromotionService promotionService;
    @Mock private FlightService flightService;

    @InjectMocks private BookingService bookingService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify booking engine PNR lookup and details resolution - Test Suite 16")
    void testGetBookingByPnrSuite16() {
        User user = User.builder().id(1L).email("user16@skynova.demo").firstName("Test16").lastName("User").build();
        Flight flight = Flight.builder().id(10L).flightNumber("SN-116").basePrice(BigDecimal.valueOf(450.00)).build();

        Booking booking = Booking.builder()
            .id((long) i)
            .pnr("PNR016")
            .user(user)
            .flight(flight)
            .tripType(TripType.ONE_WAY)
            .cabinClass(CabinClassType.ECONOMY)
            .passengerCount(1)
            .totalAmount(BigDecimal.valueOf(504.00))
            .status(BookingStatus.CONFIRMED)
            .build();

        when(bookingRepository.findByPnr("PNR016")).thenReturn(Optional.of(booking));
        when(flightService.mapToResponse(any())).thenReturn(
            com.airline.dto.FlightDTOs.FlightResponse.builder()
                .id(10L).flightNumber("SN-116").basePrice(BigDecimal.valueOf(450.00)).calculatedFare(BigDecimal.valueOf(504.00)).build()
        );

        var result = bookingService.getBookingByPnr("PNR016");
        assertNotNull(result);
        assertEquals("PNR016", result.getPnr());
        assertEquals(BookingStatus.CONFIRMED, result.getStatus());
    }
}
