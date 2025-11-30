package com.capstone.airlineticketreservationsystem.bookings.repositories;

import com.capstone.airlineticketreservationsystem.bookings.models.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepositoryDAO {

    Ticket save(Ticket ticket);

    Optional<Ticket> findByUUID(String ticketsUUID);

    Optional<Ticket> findByTicketNumber(String ticketNumber);

    List<Ticket> findByBookingId(Long bookingId);

    List<Ticket> findByPassengerId(Long passengerId);

    List<Ticket> findByFlightId(Long flightId);

    Ticket update(Ticket ticket);

    int softDeleteByUUID(String ticketsUUID);
}
