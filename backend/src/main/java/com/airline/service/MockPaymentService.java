package com.airline.service;

import com.airline.dto.PaymentDTOs;
import com.airline.entity.Booking;
import com.airline.entity.Payment;
import com.airline.entity.enums.BookingStatus;
import com.airline.entity.enums.PaymentStatus;
import com.airline.exception.PaymentProcessingException;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.BookingRepository;
import com.airline.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MockPaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final TicketService ticketService;

    private static final SecureRandom RANDOM = new SecureRandom();

    @Transactional
    public PaymentDTOs.PaymentResponse processPayment(PaymentDTOs.PaymentRequest request) {
        Booking booking = bookingRepository.findById(request.getBookingId())
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + request.getBookingId()));

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            throw new PaymentProcessingException("Booking is already paid and confirmed!");
        }

        // Mask credit card number safely
        String maskedCard = "****-****-****-4242";
        if (request.getCardNumber() != null && request.getCardNumber().length() >= 4) {
            String clean = request.getCardNumber().replaceAll("\\s+", "");
            maskedCard = "****-****-****-" + clean.substring(clean.length() - 4);
        }

        String txnRef = "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String authCode = "AUTH-" + (100000 + RANDOM.nextInt(900000));

        Payment payment = Payment.builder()
            .transactionReference(txnRef)
            .booking(booking)
            .amount(booking.getTotalAmount())
            .currency("USD")
            .paymentMethod(request.getPaymentMethod() != null ? request.getPaymentMethod() : "MOCK_CARD")
            .providerName("MOCK_PAYMENT_GATEWAY")
            .providerTransactionId("MOCK_PG_" + System.currentTimeMillis())
            .authorizationCode(authCode)
            .maskedCardNumber(maskedCard)
            .cardHolderName(request.getCardHolderName() != null ? request.getCardHolderName() : "Valued Customer")
            .status(PaymentStatus.SUCCESS)
            .build();

        Payment savedPayment = paymentRepository.save(payment);

        // Update Booking Status to CONFIRMED
        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        // Issue E-Tickets for all passengers
        ticketService.generateTicketsForBooking(booking);

        return PaymentDTOs.PaymentResponse.builder()
            .transactionReference(savedPayment.getTransactionReference())
            .bookingId(booking.getId())
            .pnr(booking.getPnr())
            .amount(savedPayment.getAmount())
            .currency(savedPayment.getCurrency())
            .paymentMethod(savedPayment.getPaymentMethod())
            .authorizationCode(savedPayment.getAuthorizationCode())
            .maskedCardNumber(savedPayment.getMaskedCardNumber())
            .status(savedPayment.getStatus())
            .createdAt(savedPayment.getCreatedAt())
            .message("Demo Payment Processed Successfully — No real money was charged.")
            .build();
    }
}
