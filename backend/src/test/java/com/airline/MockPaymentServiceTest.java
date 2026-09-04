package com.airline;

import com.airline.dto.PaymentDTOs;
import com.airline.entity.Booking;
import com.airline.entity.Payment;
import com.airline.entity.enums.BookingStatus;
import com.airline.entity.enums.PaymentStatus;
import com.airline.repository.BookingRepository;
import com.airline.repository.PaymentRepository;
import com.airline.service.MockPaymentService;
import com.airline.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MockPaymentServiceTest {

    @Mock private PaymentRepository paymentRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private TicketService ticketService;

    @InjectMocks private MockPaymentService mockPaymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Process payment authorizes transaction and updates booking to CONFIRMED")
    void testProcessPaymentSuccess() {
        Booking booking = Booking.builder()
            .id(1L)
            .pnr("K7P4M2")
            .totalAmount(BigDecimal.valueOf(560.00))
            .status(BookingStatus.PENDING_PAYMENT)
            .build();

        when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        when(paymentRepository.save(any())).thenAnswer(i -> {
            Payment p = i.getArgument(0);
            p.setId(50L);
            return p;
        });

        PaymentDTOs.PaymentRequest req = PaymentDTOs.PaymentRequest.builder()
            .bookingId(1L)
            .paymentMethod("MOCK_CARD")
            .cardHolderName("John Doe")
            .cardNumber("4242 4242 4242 4242")
            .build();

        PaymentDTOs.PaymentResponse resp = mockPaymentService.processPayment(req);

        assertNotNull(resp);
        assertEquals(PaymentStatus.SUCCESS, resp.getStatus());
        assertEquals("K7P4M2", resp.getPnr());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());
    }
}
