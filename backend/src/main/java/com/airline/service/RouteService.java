package com.airline.service;

import com.airline.entity.Airport;
import com.airline.entity.Route;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.AirportRepository;
import com.airline.repository.RouteRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final AirportRepository airportRepository;

    @Getter
    @Setter
    @Builder
    public static class RouteResponse {
        private Long id;
        private String routeCode;
        private String originIata;
        private String originCity;
        private String destinationIata;
        private String destinationCity;
        private Double distanceKm;
        private Integer estimatedDurationMinutes;
        private Boolean isActive;
    }

    @Transactional(readOnly = true)
    public List<RouteResponse> getAllRoutes() {
        return routeRepository.findAll().stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    @Transactional
    public RouteResponse createRoute(String originIata, String destIata, Double distanceKm, Integer durationMinutes) {
        Airport origin = airportRepository.findByIataCode(originIata.toUpperCase())
            .orElseThrow(() -> new ResourceNotFoundException("Origin airport not found: " + originIata));
        Airport dest = airportRepository.findByIataCode(destIata.toUpperCase())
            .orElseThrow(() -> new ResourceNotFoundException("Destination airport not found: " + destIata));

        String code = originIata.toUpperCase() + "-" + destIata.toUpperCase();

        Route route = Route.builder()
            .routeCode(code)
            .originAirport(origin)
            .destinationAirport(dest)
            .distanceKm(distanceKm)
            .estimatedDurationMinutes(durationMinutes)
            .isActive(true)
            .build();

        return mapToResponse(routeRepository.save(route));
    }

    private RouteResponse mapToResponse(Route route) {
        return RouteResponse.builder()
            .id(route.getId())
            .routeCode(route.getRouteCode())
            .originIata(route.getOriginAirport().getIataCode())
            .originCity(route.getOriginAirport().getCity())
            .destinationIata(route.getDestinationAirport().getIataCode())
            .destinationCity(route.getDestinationAirport().getCity())
            .distanceKm(route.getDistanceKm())
            .estimatedDurationMinutes(route.getEstimatedDurationMinutes())
            .isActive(route.getIsActive())
            .build();
    }
}
