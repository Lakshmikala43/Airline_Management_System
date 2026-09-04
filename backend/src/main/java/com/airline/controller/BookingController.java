package com.airline.controller;

import com.airline.dto.BookingDTOs;
import com.airline.security.SecurityUtils;
import com.airline.service.BookingService;
import com.airline.service.RefundService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;
    private final RefundService refundService;

    @PostMapping
    public ResponseEntity<BookingDTOs.BookingResponse> createBooking(@Valid @RequestBody BookingDTOs.BookingCreateRequest request) {
        String email = SecurityUtils.getCurrentUserEmail()
            .orElseThrow(() -> new IllegalStateException("Authentication required"));
        return ResponseEntity.ok(bookingService.createBooking(email, request));
    }

    @GetMapping("/pnr/{pnr}")
    public ResponseEntity<BookingDTOs.BookingResponse> getBookingByPnr(@PathVariable String pnr) {
        return ResponseEntity.ok(bookingService.getBookingByPnr(pnr));
    }

    @GetMapping("/my-bookings")
    public ResponseEntity<List<BookingDTOs.BookingResponse>> getMyBookings() {
        String email = SecurityUtils.getCurrentUserEmail()
            .orElseThrow(() -> new IllegalStateException("Authentication required"));
        return ResponseEntity.ok(bookingService.getUserBookings(email));
    }

    @PostMapping("/{pnr}/cancel")
    public ResponseEntity<BookingDTOs.CancellationResponse> cancelBooking(@PathVariable String pnr) {
        return ResponseEntity.ok(refundService.processCancellationAndRefund(pnr));
    }
}
