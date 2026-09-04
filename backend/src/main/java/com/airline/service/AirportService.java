package com.airline.service;

import com.airline.dto.AirportDTOs;
import com.airline.entity.Airport;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.AirportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirportService {

    private final AirportRepository airportRepository;

    @Transactional(readOnly = true)
    public List<AirportDTOs.AirportResponse> getAllAirports() {
        return airportRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AirportDTOs.AirportResponse> searchAirports(String query) {
        if (query == null || query.isBlank()) {
            return airportRepository.findByIsActiveTrue().stream().map(this::mapToResponse).collect(Collectors.toList());
        }
        return airportRepository.searchAirports(query).stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AirportDTOs.AirportResponse getAirportByIata(String iataCode) {
        Airport airport = airportRepository.findByIataCode(iataCode.toUpperCase())
            .orElseThrow(() -> new ResourceNotFoundException("Airport not found with IATA code: " + iataCode));
        return mapToResponse(airport);
    }

    @Transactional
    public AirportDTOs.AirportResponse createAirport(AirportDTOs.AirportRequest request) {
        if (airportRepository.existsByIataCode(request.getIataCode().toUpperCase())) {
            throw new IllegalArgumentException("Airport with IATA code already exists: " + request.getIataCode());
        }

        Airport airport = Airport.builder()
            .iataCode(request.getIataCode().toUpperCase())
            .icaoCode(request.getIcaoCode() != null ? request.getIcaoCode().toUpperCase() : null)
            .name(request.getName())
            .city(request.getCity())
            .country(request.getCountry())
            .latitude(request.getLatitude())
            .longitude(request.getLongitude())
            .timeZone(request.getTimeZone())
            .terminalInfo(request.getTerminalInfo())
            .isActive(true)
            .build();

        return mapToResponse(airportRepository.save(airport));
    }

    private AirportDTOs.AirportResponse mapToResponse(Airport airport) {
        return AirportDTOs.AirportResponse.builder()
            .id(airport.getId())
            .iataCode(airport.getIataCode())
            .icaoCode(airport.getIcaoCode())
            .name(airport.getName())
            .city(airport.getCity())
            .country(airport.getCountry())
            .latitude(airport.getLatitude())
            .longitude(airport.getLongitude())
            .timeZone(airport.getTimeZone())
            .terminalInfo(airport.getTerminalInfo())
            .isActive(airport.getIsActive())
            .build();
    }
}
