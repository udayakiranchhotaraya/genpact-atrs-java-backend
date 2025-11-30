package com.capstone.airlineticketreservationsystem.bookings.controllers;

import com.capstone.airlineticketreservationsystem.bookings.dtos.CreateTicketRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.TicketDTO;
import com.capstone.airlineticketreservationsystem.bookings.services.TicketService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {

    private final TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // ===== CREATE TICKET =====
    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody CreateTicketRequest request) {
        TicketDTO created = ticketService.createTicket(request);
        return ResponseEntity.ok(created);
    }

    // ===== GET BY UUID =====
    @GetMapping("/{ticketUUID}")
    public ResponseEntity<TicketDTO> getTicket(@PathVariable String ticketUUID) {
        return ResponseEntity.ok(ticketService.getTicketByUUID(ticketUUID));
    }

    // ===== GET ALL TICKETS OF A BOOKING =====
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<TicketDTO>> getTicketsByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(ticketService.getTicketsByBookingId(bookingId));
    }

    // ===== SOFT DELETE =====
    @DeleteMapping("/{ticketUUID}")
    public ResponseEntity<String> deleteTicket(@PathVariable String ticketUUID) {
        ticketService.deleteTicketByUUID(ticketUUID);
        return ResponseEntity.ok("Ticket deleted successfully");
    }
}
