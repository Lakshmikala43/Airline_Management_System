package com.airline.service;

import com.airline.dto.TicketDTOs;
import com.airline.entity.Booking;
import com.airline.entity.BookingPassenger;
import com.airline.entity.Passenger;
import com.airline.entity.Ticket;
import com.airline.exception.ResourceNotFoundException;
import com.airline.repository.BookingPassengerRepository;
import com.airline.repository.TicketRepository;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final BookingPassengerRepository bookingPassengerRepository;

    @Transactional
    public List<Ticket> generateTicketsForBooking(Booking booking) {
        List<BookingPassenger> bps = bookingPassengerRepository.findByBookingId(booking.getId());

        return bps.stream().map(bp -> {
            Passenger passenger = bp.getPassenger();
            String ticketNo = "TK-" + booking.getPnr() + "-" + passenger.getId();
            
            String qrContent = String.format("SKYNOVA|PNR:%s|TICKET:%s|FLIGHT:%s|PAX:%s %s",
                booking.getPnr(),
                ticketNo,
                booking.getFlight().getFlightNumber(),
                passenger.getFirstName(),
                passenger.getLastName()
            );

            String qrBase64 = generateQRCodeBase64(qrContent);

            Ticket ticket = Ticket.builder()
                .ticketNumber(ticketNo)
                .booking(booking)
                .passenger(passenger)
                .pnr(booking.getPnr())
                .qrCodeData(qrBase64)
                .pdfFilePath("/tickets/" + ticketNo + ".pdf")
                .status("ISSUED")
                .build();

            return ticketRepository.save(ticket);
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TicketDTOs.TicketVerificationResponse verifyTicket(String ticketNumber) {
        Ticket ticket = ticketRepository.findByTicketNumber(ticketNumber.toUpperCase())
            .orElse(null);

        if (ticket == null) {
            return TicketDTOs.TicketVerificationResponse.builder()
                .isValid(false)
                .ticketNumber(ticketNumber)
                .status("INVALID")
                .verificationMessage("Ticket number not found in SkyNova Airways registry.")
                .build();
        }

        Passenger p = ticket.getPassenger();
        Booking b = ticket.getBooking();

        return TicketDTOs.TicketVerificationResponse.builder()
            .isValid(true)
            .ticketNumber(ticket.getTicketNumber())
            .pnr(ticket.getPnr())
            .passengerName(p.getFirstName() + " " + p.getLastName())
            .flightNumber(b.getFlight().getFlightNumber())
            .originAirport(b.getFlight().getRoute().getOriginAirport().getIataCode())
            .destinationAirport(b.getFlight().getRoute().getDestinationAirport().getIataCode())
            .departureTime(b.getFlight().getDepartureTime())
            .status(ticket.getStatus())
            .verificationMessage("VERIFIED AUTHENTIC: SkyNova Airways Official E-Ticket")
            .build();
    }

    public String generateQRCodeBase64(String text) {
        try {
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, 200, 200);
            ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
            byte[] pngData = pngOutputStream.toByteArray();
            return "data:image/png;base64," + Base64.getEncoder().encodeToString(pngData);
        } catch (Exception e) {
            return "";
        }
    }
}
