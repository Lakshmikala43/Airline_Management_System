package com.airline.service;

import com.airline.dto.AircraftDTOs;
import com.airline.entity.AircraftSeat;
import com.airline.entity.BookingSeat;
import com.airline.entity.Flight;
import com.airline.exception.ResourceNotFoundException;
import com.airline.exception.SeatUnavailableException;
import com.airline.repository.AircraftSeatRepository;
import com.airline.repository.BookingSeatRepository;
import com.airline.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final FlightRepository flightRepository;
    private final AircraftSeatRepository aircraftSeatRepository;
    private final BookingSeatRepository bookingSeatRepository;

    @Transactional(readOnly = true)
    public AircraftDTOs.SeatMapResponse getSeatMapForFlight(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new ResourceNotFoundException("Flight not found with ID: " + flightId));

        List<AircraftSeat> aircraftSeats = aircraftSeatRepository.findByAircraftId(flight.getAircraft().getId());
        List<BookingSeat> reservedSeats = bookingSeatRepository.findByFlightId(flightId);
        Set<Long> reservedSeatIds = reservedSeats.stream().map(bs -> bs.getSeat().getId()).collect(Collectors.toSet());

        List<AircraftDTOs.SeatDTO> seatDTOs = aircraftSeats.stream().map(seat -> {
            boolean isOccupied = reservedSeatIds.contains(seat.getId());
            double surcharge = 0.0;
            if (seat.getExtraLegroom()) surcharge += 25.0;
            if (seat.getIsWindow()) surcharge += 10.0;

            return AircraftDTOs.SeatDTO.builder()
                .id(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .seatRow(seat.getSeatRow())
                .seatColumn(seat.getSeatColumn())
                .cabinClass(seat.getCabinClass())
                .isWindow(seat.getIsWindow())
                .isAisle(seat.getIsAisle())
                .isExitRow(seat.getIsExitRow())
                .extraLegroom(seat.getExtraLegroom())
                .isOccupied(isOccupied)
                .isBlocked(seat.getIsBlocked())
                .seatSurcharge(surcharge)
                .build();
        }).collect(Collectors.toList());

        return AircraftDTOs.SeatMapResponse.builder()
            .flightId(flight.getId())
            .flightNumber(flight.getFlightNumber())
            .aircraftModel(flight.getAircraft().getModel())
            .seats(seatDTOs)
            .build();
    }

    @Transactional
    public void validateSeatAvailabilityForLock(Long flightId, Long seatId) {
        if (bookingSeatRepository.existsByFlightIdAndSeatId(flightId, seatId)) {
            throw new SeatUnavailableException("Seat ID " + seatId + " is already occupied on Flight ID " + flightId);
        }
        
        // Acquire pessimistic write lock check
        bookingSeatRepository.findForLockingByFlightIdAndSeatId(flightId, seatId).ifPresent(s -> {
            throw new SeatUnavailableException("Seat ID " + seatId + " is currently locked by another customer transaction!");
        });
    }
}
