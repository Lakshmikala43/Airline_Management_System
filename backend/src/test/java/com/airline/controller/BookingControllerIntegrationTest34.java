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

class BookingControllerIntegrationTest34 {

    @Mock private BookingService bookingService;
    @Mock private RefundService refundService;

    @InjectMocks private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify get booking by PNR REST endpoint response - Integration Suite 34")
    void testGetBookingByPnrEndpoint34() {
        BookingDTOs.BookingResponse resp = BookingDTOs.BookingResponse.builder()
            .id((long) 34)
            .pnr("PNR034")
            .totalAmount(BigDecimal.valueOf(560.00))
            .status(com.airline.entity.enums.BookingStatus.CONFIRMED)
            .build();

        when(bookingService.getBookingByPnr("PNR034")).thenReturn(resp);

        ResponseEntity<BookingDTOs.BookingResponse> response = bookingController.getBookingByPnr("PNR034");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PNR034", response.getBody().getPnr());
    }

    @Test
    @DisplayName("Verify booking cancellation REST endpoint response - Integration Suite 34")
    void testCancelBookingEndpoint34() {
        BookingDTOs.CancellationResponse cancelResp = BookingDTOs.CancellationResponse.builder()
            .pnr("PNR034")
            .originalAmount(BigDecimal.valueOf(560.00))
            .cancellationFee(BigDecimal.valueOf(50.00))
            .refundAmount(BigDecimal.valueOf(510.00))
            .status("REFUNDED")
            .refundReference("REF-0034")
            .build();

        when(refundService.processCancellationAndRefund("PNR034")).thenReturn(cancelResp);

        ResponseEntity<BookingDTOs.CancellationResponse> response = bookingController.cancelBooking("PNR034");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("REFUNDED", response.getBody().getStatus());
    }
}
