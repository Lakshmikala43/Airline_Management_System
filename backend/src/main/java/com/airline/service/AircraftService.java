package com.airline.service;

import com.airline.dto.AircraftDTOs;
import com.airline.entity.Aircraft;
import com.airline.entity.Airline;
import com.airline.entity.enums.AircraftStatus;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.AircraftRepository;
import com.airline.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AircraftService {

    private final AircraftRepository aircraftRepository;
    private final AirlineRepository airlineRepository;

    @Transactional(readOnly = true)
    public List<AircraftDTOs.AircraftResponse> getAllAircraft() {
        return aircraftRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AircraftDTOs.AircraftResponse getAircraftById(Long id) {
        Aircraft aircraft = aircraftRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Aircraft not found with ID: " + id));
        return mapToResponse(aircraft);
    }

    @Transactional
    public AircraftDTOs.AircraftResponse createAircraft(AircraftDTOs.AircraftRequest request) {
        if (aircraftRepository.existsByRegistrationNumber(request.getRegistrationNumber())) {
            throw new IllegalArgumentException("Aircraft already exists with registration number: " + request.getRegistrationNumber());
        }

        Airline airline = airlineRepository.findById(request.getAirlineId())
            .orElseThrow(() -> new ResourceNotFoundException("Airline not found with ID: " + request.getAirlineId()));

        Aircraft aircraft = Aircraft.builder()
            .registrationNumber(request.getRegistrationNumber())
            .airline(airline)
            .manufacturer(request.getManufacturer())
            .model(request.getModel())
            .totalCapacity(request.getTotalCapacity())
            .economyCapacity(request.getEconomyCapacity() != null ? request.getEconomyCapacity() : 0)
            .premiumEconomyCapacity(request.getPremiumEconomyCapacity() != null ? request.getPremiumEconomyCapacity() : 0)
            .businessCapacity(request.getBusinessCapacity() != null ? request.getBusinessCapacity() : 0)
            .firstClassCapacity(request.getFirstClassCapacity() != null ? request.getFirstClassCapacity() : 0)
            .status(AircraftStatus.ACTIVE)
            .manufactureYear(request.getManufactureYear())
            .build();

        return mapToResponse(aircraftRepository.save(aircraft));
    }

    private AircraftDTOs.AircraftResponse mapToResponse(Aircraft aircraft) {
        return AircraftDTOs.AircraftResponse.builder()
            .id(aircraft.getId())
            .registrationNumber(aircraft.getRegistrationNumber())
            .airlineName(aircraft.getAirline() != null ? aircraft.getAirline().getName() : "SkyNova")
            .manufacturer(aircraft.getManufacturer())
            .model(aircraft.getModel())
            .totalCapacity(aircraft.getTotalCapacity())
            .economyCapacity(aircraft.getEconomyCapacity())
            .premiumEconomyCapacity(aircraft.getPremiumEconomyCapacity())
            .businessCapacity(aircraft.getBusinessCapacity())
            .firstClassCapacity(aircraft.getFirstClassCapacity())
            .status(aircraft.getStatus())
            .manufactureYear(aircraft.getManufactureYear())
            .build();
    }
}
