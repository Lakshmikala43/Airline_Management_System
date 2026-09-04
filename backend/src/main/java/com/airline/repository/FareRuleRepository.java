package com.airline.repository;

import com.airline.entity.FareRule;
import com.airline.entity.enums.CabinClassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FareRuleRepository extends JpaRepository<FareRule, Long> {
    List<FareRule> findByFlightId(Long flightId);
    Optional<FareRule> findByFlightIdAndCabinClass(Long flightId, CabinClassType cabinClass);
}
