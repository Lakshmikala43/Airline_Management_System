package com.airline.repository;

import com.airline.entity.BookingSeat;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
    
    List<BookingSeat> findByFlightId(Long flightId);
    
    List<BookingSeat> findByBookingId(Long bookingId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT bs FROM BookingSeat bs WHERE bs.flight.id = :flightId AND bs.seat.id = :seatId")
    Optional<BookingSeat> findForLockingByFlightIdAndSeatId(
        @Param("flightId") Long flightId, 
        @Param("seatId") Long seatId
    );

    Boolean existsByFlightIdAndSeatId(Long flightId, Long seatId);
}
