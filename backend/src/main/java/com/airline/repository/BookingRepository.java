package com.airline.repository;

import com.airline.entity.Booking;
import com.airline.entity.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByPnr(String pnr);
    Optional<Booking> findByPnrAndUserEmail(String pnr, String email);
    List<Booking> findByUserId(Long userId);
    Page<Booking> findByUserId(Long userId, Pageable pageable);
    List<Booking> findByFlightId(Long flightId);
    Page<Booking> findByStatus(BookingStatus status, Pageable pageable);

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status IN ('CONFIRMED', 'COMPLETED')")
    BigDecimal calculateTotalRevenue();

    @Query("SELECT SUM(b.totalAmount) FROM Booking b WHERE b.status IN ('CONFIRMED', 'COMPLETED') AND b.bookingDate >= :startDate")
    BigDecimal calculateRevenueSince(@Param("startDate") ZonedDateTime startDate);

    Long countByStatus(BookingStatus status);
}
