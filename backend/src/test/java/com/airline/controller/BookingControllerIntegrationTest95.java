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

class BookingControllerIntegrationTest95 {

    @Mock private BookingService bookingService;
    @Mock private RefundService refundService;

    @InjectMocks private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verify get booking by PNR REST endpoint response - Integration Suite 95")
    void testGetBookingByPnrEndpoint95() {
        BookingDTOs.BookingResponse resp = BookingDTOs.BookingResponse.builder()
            .id((long) 95)
            .pnr("PNR095")
            .totalAmount(BigDecimal.valueOf(560.00))
            .status(com.airline.entity.enums.BookingStatus.CONFIRMED)
            .build();

        when(bookingService.getBookingByPnr("PNR095")).thenReturn(resp);

        ResponseEntity<BookingDTOs.BookingResponse> response = bookingController.getBookingByPnr("PNR095");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("PNR095", response.getBody().getPnr());
    }

    @Test
    @DisplayName("Verify booking cancellation REST endpoint response - Integration Suite 95")
    void testCancelBookingEndpoint95() {
        BookingDTOs.CancellationResponse cancelResp = BookingDTOs.CancellationResponse.builder()
            .pnr("PNR095")
            .originalAmount(BigDecimal.valueOf(560.00))
            .cancellationFee(BigDecimal.valueOf(50.00))
            .refundAmount(BigDecimal.valueOf(510.00))
            .status("REFUNDED")
            .refundReference("REF-0095")
            .build();

        when(refundService.processCancellationAndRefund("PNR095")).thenReturn(cancelResp);

        ResponseEntity<BookingDTOs.CancellationResponse> response = bookingController.cancelBooking("PNR095");
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("REFUNDED", response.getBody().getStatus());
    }
}
