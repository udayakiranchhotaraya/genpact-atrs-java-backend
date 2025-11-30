package com.capstone.airlineticketreservationsystem.bookings.services;

import com.capstone.airlineticketreservationsystem.bookings.dtos.CreateTicketRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.TicketDTO;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.TicketNotFoundException;
import com.capstone.airlineticketreservationsystem.bookings.models.SeatClass;
import com.capstone.airlineticketreservationsystem.bookings.models.Ticket;
import com.capstone.airlineticketreservationsystem.bookings.models.TicketStatus;
import com.capstone.airlineticketreservationsystem.bookings.repositories.TicketRepositoryDAO;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketService {

    private final TicketRepositoryDAO ticketRepository;

    public TicketService(TicketRepositoryDAO ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    // ================= CREATE TICKET ====================
    public TicketDTO createTicket(CreateTicketRequest request) {

        Ticket ticket = new Ticket();
        ticket.setTicketNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 12));

        ticket.setBookingId(request.getBookingId());
        ticket.setPassengerId(request.getPassengerId());
        ticket.setFlightId(request.getFlightId());

        // Seat class now using ENUM directly
        ticket.setSeatClass(request.getSeatClass());

        ticket.setBaseFare(request.getBaseFare());
        ticket.setTaxes(request.getTaxes());
        ticket.setAncillaryCharges(BigDecimal.ZERO);
        ticket.setTotalFare(request.getBaseFare().add(request.getTaxes()));
        ticket.setTicketStatus(TicketStatus.ISSUED);

        Ticket saved = ticketRepository.save(ticket);
        return convertToDTO(saved);
    }

    // ================= GET BY UUID ====================
    public TicketDTO getTicketByUUID(String ticketUUID) {
        Ticket ticket = ticketRepository.findByUUID(ticketUUID)
                .orElseThrow(() ->
                        new TicketNotFoundException("Ticket not found with UUID: " + ticketUUID));

        return convertToDTO(ticket);
    }

    // ================= GET BY BOOKING ====================
    public List<TicketDTO> getTicketsByBookingId(Long bookingId) {
        return ticketRepository.findByBookingId(bookingId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ================= DELETE ====================
    public void deleteTicketByUUID(String ticketUUID) {
        int rows = ticketRepository.softDeleteByUUID(ticketUUID);
        if (rows == 0) {
            throw new TicketNotFoundException("Ticket not found or already deleted: " + ticketUUID);
        }
    }

    // ================= CONVERTER ====================
    private TicketDTO convertToDTO(Ticket t) {
        TicketDTO dto = new TicketDTO();

        dto.setTicketsUUID(t.getTicketsUUID());
        dto.setTicketNumber(t.getTicketNumber());
        dto.setBookingId(t.getBookingId());
        dto.setPassengerId(t.getPassengerId());
        dto.setFlightId(t.getFlightId());

        dto.setSeatClass(t.getSeatClass().name()); // ENUM → string

        dto.setBaseFare(t.getBaseFare());
        dto.setTaxes(t.getTaxes());
        dto.setTotalFare(t.getTotalFare());
        dto.setTicketStatus(t.getTicketStatus().name());
        dto.setCreatedAt(t.getCreatedAt());

        return dto;
    }
}
