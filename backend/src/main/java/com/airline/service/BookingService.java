package com.airline.service;

import com.airline.dto.BookingDTOs;
import com.airline.dto.FlightDTOs;
import com.airline.dto.PassengerDTOs;
import com.airline.entity.*;
import com.airline.entity.enums.BookingStatus;
import com.airline.exception.InvalidBookingStateException;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final FlightRepository flightRepository;
    private final UserRepository userRepository;
    private final PassengerRepository passengerRepository;
    private final BookingPassengerRepository bookingPassengerRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final AircraftSeatRepository aircraftSeatRepository;
    private final FareCalculationService fareCalculationService;
    private final SeatService seatService;
    private final PromotionService promotionService;
    private final FlightService flightService;

    private static final String PNR_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private String generateUniquePNR() {
        String pnr;
        do {
            StringBuilder sb = new StringBuilder(6);
            for (int i = 0; i < 6; i++) {
                sb.append(PNR_CHARS.charAt(RANDOM.nextInt(PNR_CHARS.length())));
            }
            pnr = sb.toString();
        } while (bookingRepository.findByPnr(pnr).isPresent());
        return pnr;
    }

    @Transactional
    public BookingDTOs.BookingResponse createBooking(String userEmail, BookingDTOs.BookingCreateRequest request) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        Flight flight = flightRepository.findById(request.getFlightId())
            .orElseThrow(() -> new ResourceNotFoundException("Flight not found: " + request.getFlightId()));

        Flight returnFlight = null;
        if (request.getReturnFlightId() != null) {
            returnFlight = flightRepository.findById(request.getReturnFlightId())
                .orElseThrow(() -> new ResourceNotFoundException("Return flight not found: " + request.getReturnFlightId()));
        }

        // Validate seats selected for passengers and prevent double-booking
        BigDecimal totalSeatFee = BigDecimal.ZERO;
        for (var passengerReq : request.getPassengers()) {
            if (passengerReq.getSelectedSeatId() != null) {
                seatService.validateSeatAvailabilityForLock(flight.getId(), passengerReq.getSelectedSeatId());
                AircraftSeat seat = aircraftSeatRepository.findById(passengerReq.getSelectedSeatId())
                    .orElseThrow(() -> new ResourceNotFoundException("Seat not found: " + passengerReq.getSelectedSeatId()));
                if (seat.getExtraLegroom()) totalSeatFee = totalSeatFee.add(BigDecimal.valueOf(25.00));
                if (seat.getIsWindow()) totalSeatFee = totalSeatFee.add(BigDecimal.valueOf(10.00));
            }
        }

        // Coupon discount calculation
        BigDecimal discount = BigDecimal.ZERO;
        if (request.getCouponCode() != null && !request.getCouponCode().isBlank()) {
            var promoValidation = promotionService.validateCoupon(request.getCouponCode(), flight.getBasePrice());
            if (promoValidation.getIsValid()) {
                discount = promoValidation.getCalculatedDiscountAmount();
            }
        }

        var fareBreakdown = fareCalculationService.calculateFare(
            flight,
            request.getCabinClass(),
            request.getPassengers().size(),
            totalSeatFee,
            discount
        );

        Booking booking = Booking.builder()
            .pnr(generateUniquePNR())
            .user(user)
            .flight(flight)
            .returnFlight(returnFlight)
            .tripType(request.getTripType())
            .cabinClass(request.getCabinClass())
            .passengerCount(request.getPassengers().size())
            .baseFare(fareBreakdown.getTotalBaseFare())
            .taxAmount(fareBreakdown.getTaxAmount())
            .seatFee(fareBreakdown.getSeatFee())
            .baggageFee(fareBreakdown.getBaggageFee())
            .discountAmount(fareBreakdown.getDiscountAmount())
            .totalAmount(fareBreakdown.getFinalTotalAmount())
            .status(BookingStatus.PENDING_PAYMENT)
            .build();

        Booking savedBooking = bookingRepository.save(booking);

        // Process passengers and associate seats
        List<PassengerDTOs.PassengerResponse> passengerResponses = new ArrayList<>();
        for (var pReq : request.getPassengers()) {
            Passenger passenger = Passenger.builder()
                .title(pReq.getTitle())
                .firstName(pReq.getFirstName())
                .middleName(pReq.getMiddleName())
                .lastName(pReq.getLastName())
                .dateOfBirth(pReq.getDateOfBirth())
                .gender(pReq.getGender())
                .nationality(pReq.getNationality())
                .passportNumber(pReq.getPassportNumber())
                .passportExpiry(pReq.getPassportExpiry())
                .email(pReq.getEmail() != null ? pReq.getEmail() : userEmail)
                .phoneNumber(pReq.getPhoneNumber())
                .build();
            Passenger savedPassenger = passengerRepository.save(passenger);

            BookingPassenger bp = BookingPassenger.builder()
                .booking(savedBooking)
                .passenger(savedPassenger)
                .specialRequest(pReq.getSpecialRequest())
                .build();
            bookingPassengerRepository.save(bp);

            String assignedSeatNumber = "Unassigned";
            if (pReq.getSelectedSeatId() != null) {
                AircraftSeat seat = aircraftSeatRepository.findById(pReq.getSelectedSeatId()).orElseThrow();
                BookingSeat bookingSeat = BookingSeat.builder()
                    .booking(savedBooking)
                    .flight(flight)
                    .seat(seat)
                    .passenger(savedPassenger)
                    .seatPrice(BigDecimal.ZERO)
                    .build();
                bookingSeatRepository.save(bookingSeat);
                assignedSeatNumber = seat.getSeatNumber();
            }

            passengerResponses.add(PassengerDTOs.PassengerResponse.builder()
                .id(savedPassenger.getId())
                .title(savedPassenger.getTitle())
                .firstName(savedPassenger.getFirstName())
                .lastName(savedPassenger.getLastName())
                .dateOfBirth(savedPassenger.getDateOfBirth())
                .gender(savedPassenger.getGender())
                .nationality(savedPassenger.getNationality())
                .passportNumber(savedPassenger.getPassportNumber())
                .passportExpiry(savedPassenger.getPassportExpiry())
                .email(savedPassenger.getEmail())
                .phoneNumber(savedPassenger.getPhoneNumber())
                .seatNumber(assignedSeatNumber)
                .build());
        }

        return mapToResponse(savedBooking, passengerResponses);
    }

    @Transactional(readOnly = true)
    public BookingDTOs.BookingResponse getBookingByPnr(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr.toUpperCase())
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with PNR: " + pnr));
        return mapToResponse(booking, getPassengersForBooking(booking.getId()));
    }

    @Transactional(readOnly = true)
    public List<BookingDTOs.BookingResponse> getUserBookings(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        return bookingRepository.findByUserId(user.getId()).stream()
            .map(b -> mapToResponse(b, getPassengersForBooking(b.getId())))
            .collect(Collectors.toList());
    }

    @Transactional
    public BookingDTOs.BookingResponse cancelBooking(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr.toUpperCase())
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found with PNR: " + pnr));

        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.REFUNDED) {
            throw new InvalidBookingStateException("Booking is already cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);
        Booking updated = bookingRepository.save(booking);
        return mapToResponse(updated, getPassengersForBooking(updated.getId()));
    }

    private List<PassengerDTOs.PassengerResponse> getPassengersForBooking(Long bookingId) {
        List<BookingPassenger> bps = bookingPassengerRepository.findByBookingId(bookingId);
        List<BookingSeat> seats = bookingSeatRepository.findByBookingId(bookingId);

        return bps.stream().map(bp -> {
            Passenger p = bp.getPassenger();
            String seatNo = seats.stream()
                .filter(s -> s.getPassenger().getId().equals(p.getId()))
                .map(s -> s.getSeat().getSeatNumber())
                .findFirst().orElse("Unassigned");

            return PassengerDTOs.PassengerResponse.builder()
                .id(p.getId())
                .title(p.getTitle())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .dateOfBirth(p.getDateOfBirth())
                .gender(p.getGender())
                .nationality(p.getNationality())
                .passportNumber(p.getPassportNumber())
                .passportExpiry(p.getPassportExpiry())
                .email(p.getEmail())
                .phoneNumber(p.getPhoneNumber())
                .seatNumber(seatNo)
                .build();
        }).collect(Collectors.toList());
    }

    public BookingDTOs.BookingResponse mapToResponse(Booking booking, List<PassengerDTOs.PassengerResponse> passengers) {
        FlightDTOs.FlightResponse flightResp = flightService.mapToResponse(booking.getFlight());
        FlightDTOs.FlightResponse returnFlightResp = booking.getReturnFlight() != null ? flightService.mapToResponse(booking.getReturnFlight()) : null;

        return BookingDTOs.BookingResponse.builder()
            .id(booking.getId())
            .pnr(booking.getPnr())
            .customerEmail(booking.getUser().getEmail())
            .customerName(booking.getUser().getFirstName() + " " + booking.getUser().getLastName())
            .flight(flightResp)
            .returnFlight(returnFlightResp)
            .tripType(booking.getTripType())
            .cabinClass(booking.getCabinClass())
            .passengerCount(booking.getPassengerCount())
            .baseFare(booking.getBaseFare())
            .taxAmount(booking.getTaxAmount())
            .seatFee(booking.getSeatFee())
            .baggageFee(booking.getBaggageFee())
            .discountAmount(booking.getDiscountAmount())
            .totalAmount(booking.getTotalAmount())
            .status(booking.getStatus())
            .bookingDate(booking.getBookingDate())
            .passengers(passengers)
            .paymentStatus(booking.getStatus().name())
            .ticketNumber("TK-" + booking.getPnr())
            .build();
    }
}
