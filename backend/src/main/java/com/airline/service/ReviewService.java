package com.airline.service;

import com.airline.entity.Flight;
import com.airline.entity.Review;
import com.airline.entity.User;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.FlightRepository;
import com.airline.repository.ReviewRepository;
import com.airline.repository.UserRepository;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;

    @Getter
    @Builder
    public static class ReviewResponse {
        private Long id;
        private String customerName;
        private String flightNumber;
        private Integer rating;
        private String comment;
        private ZonedDateTime createdAt;
    }

    @Transactional
    public ReviewResponse addReview(String userEmail, Long flightId, Integer rating, String comment) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userEmail));
        Flight flight = flightRepository.findById(flightId)
            .orElseThrow(() -> new ResourceNotFoundException("Flight not found: " + flightId));

        Review review = Review.builder()
            .user(user)
            .flight(flight)
            .rating(rating)
            .comment(comment)
            .status("APPROVED")
            .build();

        Review saved = reviewRepository.save(review);
        return ReviewResponse.builder()
            .id(saved.getId())
            .customerName(user.getFirstName() + " " + user.getLastName().charAt(0) + ".")
            .flightNumber(flight.getFlightNumber())
            .rating(saved.getRating())
            .comment(saved.getComment())
            .createdAt(saved.getCreatedAt())
            .build();
    }

    @Transactional(readOnly = true)
    public List<ReviewResponse> getFlightReviews(Long flightId) {
        return reviewRepository.findByFlightIdAndStatus(flightId, "APPROVED").stream()
            .map(r -> ReviewResponse.builder()
                .id(r.getId())
                .customerName(r.getUser().getFirstName() + " " + r.getUser().getLastName().charAt(0) + ".")
                .flightNumber(r.getFlight().getFlightNumber())
                .rating(r.getRating())
                .comment(r.getComment())
                .createdAt(r.getCreatedAt())
                .build())
            .collect(Collectors.toList());
    }
}
