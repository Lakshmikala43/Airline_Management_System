package com.airline.service;

import com.airline.dto.BookingDTOs;
import com.airline.entity.Booking;
import com.airline.entity.Payment;
import com.airline.entity.Refund;
import com.airline.entity.enums.BookingStatus;
import com.airline.entity.enums.RefundStatus;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.BookingRepository;
import com.airline.repository.PaymentRepository;
import com.airline.repository.RefundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefundService {

    private final RefundRepository refundRepository;
    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    @Transactional
    public BookingDTOs.CancellationResponse processCancellationAndRefund(String pnr) {
        Booking booking = bookingRepository.findByPnr(pnr.toUpperCase())
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found: " + pnr));

        Payment payment = paymentRepository.findByBookingId(booking.getId())
            .orElseThrow(() -> new ResourceNotFoundException("Payment record not found for booking: " + pnr));

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime departure = booking.getFlight().getDepartureTime();
        long hoursUntilDeparture = Duration.between(now, departure).toHours();

        BigDecimal originalAmount = booking.getTotalAmount();
        BigDecimal cancellationFee;

        if (hoursUntilDeparture > 48) {
            cancellationFee = BigDecimal.valueOf(50.00); // Flat fee
        } else if (hoursUntilDeparture >= 24) {
            cancellationFee = originalAmount.multiply(BigDecimal.valueOf(0.30)).setScale(2, RoundingMode.HALF_UP); // 30% fee
        } else {
            cancellationFee = originalAmount.multiply(BigDecimal.valueOf(0.75)).setScale(2, RoundingMode.HALF_UP); // 75% fee
        }

        BigDecimal refundAmount = originalAmount.subtract(cancellationFee);
        if (refundAmount.compareTo(BigDecimal.ZERO) < 0) {
            refundAmount = BigDecimal.ZERO;
        }

        String refundRef = "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        Refund refund = Refund.builder()
            .refundReference(refundRef)
            .booking(booking)
            .payment(payment)
            .originalAmount(originalAmount)
            .cancellationFee(cancellationFee)
            .refundAmount(refundAmount)
            .reason("Customer Requested Flight Cancellation (" + hoursUntilDeparture + "h before departure)")
            .status(RefundStatus.PROCESSED)
            .build();

        refundRepository.save(refund);

        booking.setStatus(BookingStatus.REFUNDED);
        bookingRepository.save(booking);

        return BookingDTOs.CancellationResponse.builder()
            .pnr(booking.getPnr())
            .originalAmount(originalAmount)
            .cancellationFee(cancellationFee)
            .refundAmount(refundAmount)
            .status("REFUNDED")
            .refundReference(refundRef)
            .build();
    }
}
