package com.airline.service;

import com.airline.dto.FlightDTOs;
import com.airline.entity.*;
import com.airline.entity.enums.FlightStatus;

import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final AirlineRepository airlineRepository;
    private final RouteRepository routeRepository;
    private final AircraftRepository aircraftRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final FareCalculationService fareCalculationService;

    @Transactional(readOnly = true)
    public List<FlightDTOs.FlightResponse> searchFlights(FlightDTOs.FlightSearchRequest request) {
        LocalDate date = request.getDepartureDate();
        ZonedDateTime startTime = date.atStartOfDay(ZoneId.of("UTC"));
        ZonedDateTime endTime = date.atTime(LocalTime.MAX).atZone(ZoneId.of("UTC"));

        List<Flight> flights = flightRepository.searchFlights(
            request.getOriginAirportIata().toUpperCase(),
            request.getDestinationAirportIata().toUpperCase(),
            startTime,
            endTime
        );

        return flights.stream().map(flight -> {
            FlightDTOs.FlightResponse response = mapToResponse(flight);
            
            // Calculate dynamic fare breakdown for requested cabin class
            var fare = fareCalculationService.calculateFare(
                flight,
                request.getCabinClass(),
                request.getPassengerCount(),
                null,
                null
            );
            response.setCalculatedFare(fare.getFinalTotalAmount());
            
            // Available seats calculation
            long reservedCount = bookingSeatRepository.findByFlightId(flight.getId()).size();
            int remaining = Math.max(0, flight.getAircraft().getTotalCapacity() - (int) reservedCount);
            response.setAvailableSeats(remaining);

            return response;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FlightDTOs.FlightResponse> getAllFlights() {
        return flightRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FlightDTOs.FlightResponse getFlightById(Long id) {
        Flight flight = flightRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Flight not found with ID: " + id));
        return mapToResponse(flight);
    }

    @Transactional
    public FlightDTOs.FlightResponse createFlight(FlightDTOs.FlightRequest request) {
        if (flightRepository.existsByFlightNumber(request.getFlightNumber())) {
            throw new IllegalArgumentException("Flight with flight number already exists: " + request.getFlightNumber());
        }

        Airline airline = airlineRepository.findById(request.getAirlineId())
            .orElseThrow(() -> new ResourceNotFoundException("Airline not found: " + request.getAirlineId()));
        Route route = routeRepository.findById(request.getRouteId())
            .orElseThrow(() -> new ResourceNotFoundException("Route not found: " + request.getRouteId()));
        Aircraft aircraft = aircraftRepository.findById(request.getAircraftId())
            .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found: " + request.getAircraftId()));

        Flight flight = Flight.builder()
            .flightNumber(request.getFlightNumber())
            .airline(airline)
            .route(route)
            .aircraft(aircraft)
            .departureTime(request.getDepartureTime())
            .arrivalTime(request.getArrivalTime())
            .basePrice(request.getBasePrice())
            .taxAmount(request.getTaxAmount() != null ? request.getTaxAmount() : request.getBasePrice().multiply(java.math.BigDecimal.valueOf(0.12)))
            .status(FlightStatus.SCHEDULED)
            .gateNumber(request.getGateNumber())
            .terminal(request.getTerminal())
            .delayMinutes(0)
            .build();

        return mapToResponse(flightRepository.save(flight));
    }

    @Transactional
    public FlightDTOs.FlightResponse updateFlightStatus(Long flightId, FlightDTOs.FlightStatusUpdateRequest request) {
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new ResourceNotFoundException("Flight not found with ID: " + flightId));

        flight.setStatus(request.getStatus());
        if (request.getGateNumber() != null) flight.setGateNumber(request.getGateNumber());
        if (request.getTerminal() != null) flight.setTerminal(request.getTerminal());
        if (request.getDelayMinutes() != null) flight.setDelayMinutes(request.getDelayMinutes());
        if (request.getCancellationReason() != null) flight.setCancellationReason(request.getCancellationReason());

        return mapToResponse(flightRepository.save(flight));
    }

    public FlightDTOs.FlightResponse mapToResponse(Flight flight) {
        long duration = java.time.Duration.between(flight.getDepartureTime(), flight.getArrivalTime()).toMinutes();

        return FlightDTOs.FlightResponse.builder()
            .id(flight.getId())
            .flightNumber(flight.getFlightNumber())
            .airlineName(flight.getAirline().getName())
            .airlineCode(flight.getAirline().getCode())
            .originAirportCode(flight.getRoute().getOriginAirport().getIataCode())
            .originAirportName(flight.getRoute().getOriginAirport().getName())
            .originCity(flight.getRoute().getOriginAirport().getCity())
            .destinationAirportCode(flight.getRoute().getDestinationAirport().getIataCode())
            .destinationAirportName(flight.getRoute().getDestinationAirport().getName())
            .destinationCity(flight.getRoute().getDestinationAirport().getCity())
            .aircraftModel(flight.getAircraft().getModel())
            .departureTime(flight.getDepartureTime())
            .arrivalTime(flight.getArrivalTime())
            .durationMinutes((int) duration)
            .basePrice(flight.getBasePrice())
            .calculatedFare(flight.getBasePrice())
            .taxAmount(flight.getTaxAmount())
            .status(flight.getStatus())
            .gateNumber(flight.getGateNumber())
            .terminal(flight.getTerminal())
            .availableSeats(flight.getAircraft().getTotalCapacity())
            .delayMinutes(flight.getDelayMinutes())
            .build();
    }
}
