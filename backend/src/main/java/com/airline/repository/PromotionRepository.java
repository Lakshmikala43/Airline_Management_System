package com.airline.repository;

import com.airline.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    Optional<Promotion> findByCouponCode(String couponCode);
    Boolean existsByCouponCode(String couponCode);
}
