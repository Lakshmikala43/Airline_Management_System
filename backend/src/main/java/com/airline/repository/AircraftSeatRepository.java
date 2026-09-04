package com.airline.repository;

import com.airline.entity.AircraftSeat;
import com.airline.entity.enums.CabinClassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AircraftSeatRepository extends JpaRepository<AircraftSeat, Long> {
    List<AircraftSeat> findByAircraftId(Long aircraftId);
    List<AircraftSeat> findByAircraftIdAndCabinClass(Long aircraftId, CabinClassType cabinClass);
    Optional<AircraftSeat> findByAircraftIdAndSeatNumber(Long aircraftId, String seatNumber);
}
