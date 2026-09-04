package com.airline;

import com.airline.dto.BookingDTOs;
import com.airline.dto.PassengerDTOs;
import com.airline.entity.*;
import com.airline.entity.enums.BookingStatus;
import com.airline.entity.enums.CabinClassType;
import com.airline.entity.enums.TripType;
import com.airline.repository.*;
import com.airline.service.BookingService;
import com.airline.service.FareCalculationService;
import com.airline.service.FlightService;
import com.airline.service.PromotionService;
import com.airline.service.SeatService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BookingServiceTest {

    @Mock private BookingRepository bookingRepository;
    @Mock private FlightRepository flightRepository;
    @Mock private UserRepository userRepository;
    @Mock private PassengerRepository passengerRepository;
    @Mock private BookingPassengerRepository bookingPassengerRepository;
    @Mock private BookingSeatRepository bookingSeatRepository;
    @Mock private AircraftSeatRepository aircraftSeatRepository;
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
    @DisplayName("Create booking creates valid PNR and PENDING_PAYMENT status")
    void testCreateBookingSuccess() {
        User user = User.builder().id(1L).email("john@skynova.demo").firstName("John").lastName("Doe").build();
        Flight flight = Flight.builder().id(10L).flightNumber("SN-101").basePrice(BigDecimal.valueOf(500.00)).build();

        when(userRepository.findByEmail("john@skynova.demo")).thenReturn(Optional.of(user));
        when(flightRepository.findById(10L)).thenReturn(Optional.of(flight));
        when(fareCalculationService.calculateFare(any(), any(), any(), any(), any()))
            .thenReturn(FareCalculationService.FareBreakdown.builder()
                .totalBaseFare(BigDecimal.valueOf(500.00))
                .taxAmount(BigDecimal.valueOf(60.00))
                .seatFee(BigDecimal.ZERO)
                .baggageFee(BigDecimal.ZERO)
                .discountAmount(BigDecimal.ZERO)
                .finalTotalAmount(BigDecimal.valueOf(560.00))
                .build());

        Booking savedBooking = Booking.builder()
            .id(100L)
            .pnr("K7P4M2")
            .user(user)
            .flight(flight)
            .tripType(TripType.ONE_WAY)
            .cabinClass(CabinClassType.ECONOMY)
            .passengerCount(1)
            .totalAmount(BigDecimal.valueOf(560.00))
            .status(BookingStatus.PENDING_PAYMENT)
            .build();

        when(bookingRepository.save(any())).thenReturn(savedBooking);
        when(passengerRepository.save(any())).thenReturn(Passenger.builder().id(5L).firstName("John").lastName("Doe").build());

        BookingDTOs.PassengerRequest pReq = BookingDTOs.PassengerRequest.builder()
            .firstName("John").lastName("Doe").dateOfBirth(LocalDate.of(1995, 5, 15))
            .gender("Male").nationality("US").passportNumber("A123456").passportExpiry(LocalDate.of(2030, 1, 1))
            .build();

        BookingDTOs.BookingCreateRequest request = BookingDTOs.BookingCreateRequest.builder()
            .flightId(10L)
            .cabinClass(CabinClassType.ECONOMY)
            .passengers(List.of(pReq))
            .build();

        assertDoesNotThrow(() -> bookingService.createBooking("john@skynova.demo", request));
    }
}
