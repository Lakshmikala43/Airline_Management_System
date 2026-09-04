package com.airline.repository;

import com.airline.entity.CabinClass;
import com.airline.entity.enums.CabinClassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CabinClassRepository extends JpaRepository<CabinClass, Long> {
    Optional<CabinClass> findByCode(CabinClassType code);
}
