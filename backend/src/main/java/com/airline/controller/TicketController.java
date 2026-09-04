package com.airline.controller;

import com.airline.dto.TicketDTOs;
import com.airline.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService ticketService;

    @GetMapping("/verify/{ticketNumber}")
    public ResponseEntity<TicketDTOs.TicketVerificationResponse> verifyTicket(@PathVariable String ticketNumber) {
        return ResponseEntity.ok(ticketService.verifyTicket(ticketNumber));
    }
}
