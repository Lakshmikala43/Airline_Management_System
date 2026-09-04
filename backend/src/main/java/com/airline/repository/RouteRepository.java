package com.airline.repository;

import com.airline.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findByRouteCode(String routeCode);
    Optional<Route> findByOriginAirportIataCodeAndDestinationAirportIataCode(String originIata, String destinationIata);
    List<Route> findByIsActiveTrue();
}
