package com.airline.repository;

import com.airline.entity.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long> {
    Optional<Refund> findByRefundReference(String refundReference);
    Optional<Refund> findByBookingId(Long bookingId);
}
