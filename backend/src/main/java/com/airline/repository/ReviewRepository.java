package com.airline.repository;

import com.airline.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByFlightIdAndStatus(Long flightId, String status);
    List<Review> findByUserId(Long userId);
}
