package com.airline.controller;

import com.airline.dto.BookingDTOs;
import com.airline.service.BookingService;
import com.airline.service.RefundService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class BookingControllerIntegrationTest54 {

    @Mock private BookingService bookingService;
    @Mock private RefundService refundService;

    @InjectMocks private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify get booking by PNR REST endpoint response - Integration Suite 54")
    void testGetBookingByPnrEndpoint54() {
        BookingDTOs.BookingResponse resp = BookingDTOs.BookingResponse.builder()
            .id((long) 54)
            .pnr("PNR054")
            .totalAmount(BigDecimal.valueOf(560.00))
            .status(com.airline.entity.enums.BookingStatus.CONFIRMED)
            .build();

        when(bookingService.getBookingByPnr("PNR054")).thenReturn(resp);

        ResponseEntity<BookingDTOs.BookingResponse> response = bookingController.getBookingByPnr("PNR054");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PNR054", response.getBody().getPnr());
    }

    @Test
    @DisplayName("Verify booking cancellation REST endpoint response - Integration Suite 54")
    void testCancelBookingEndpoint54() {
        BookingDTOs.CancellationResponse cancelResp = BookingDTOs.CancellationResponse.builder()
            .pnr("PNR054")
            .originalAmount(BigDecimal.valueOf(560.00))
            .cancellationFee(BigDecimal.valueOf(50.00))
            .refundAmount(BigDecimal.valueOf(510.00))
            .status("REFUNDED")
            .refundReference("REF-0054")
            .build();

        when(refundService.processCancellationAndRefund("PNR054")).thenReturn(cancelResp);

        ResponseEntity<BookingDTOs.CancellationResponse> response = bookingController.cancelBooking("PNR054");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("REFUNDED", response.getBody().getStatus());
    }
}
