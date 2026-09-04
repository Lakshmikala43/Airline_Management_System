package com.airline.controller;

import com.airline.security.SecurityUtils;
import com.airline.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<ReviewService.ReviewResponse>> getFlightReviews(@PathVariable Long flightId) {
        return ResponseEntity.ok(reviewService.getFlightReviews(flightId));
    }

    @PostMapping("/flight/{flightId}")
    public ResponseEntity<ReviewService.ReviewResponse> addReview(
            @PathVariable Long flightId,
            @RequestParam Integer rating,
            @RequestParam(required = false) String comment) {
        String email = SecurityUtils.getCurrentUserEmail()
            .orElseThrow(() -> new IllegalStateException("Authentication required"));
        return ResponseEntity.ok(reviewService.addReview(email, flightId, rating, comment));
    }
}
