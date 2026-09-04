package com.airline.repository;

import com.airline.entity.Flight;
import com.airline.entity.enums.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    Optional<Flight> findByFlightNumber(String flightNumber);
    Boolean existsByFlightNumber(String flightNumber);

    @Query("SELECT f FROM Flight f WHERE f.route.originAirport.iataCode = :origin " +
           "AND f.route.destinationAirport.iataCode = :destination " +
           "AND f.departureTime >= :startTime AND f.departureTime <= :endTime " +
           "AND f.status = 'SCHEDULED'")
    List<Flight> searchFlights(
        @Param("origin") String origin,
        @Param("destination") String destination,
        @Param("startTime") ZonedDateTime startTime,
        @Param("endTime") ZonedDateTime endTime
    );

    Page<Flight> findByStatus(FlightStatus status, Pageable pageable);
    List<Flight> findByDepartureTimeBetween(ZonedDateTime start, ZonedDateTime end);
}
