package com.capstone.airlineticketreservationsystem.bookings.exceptions;

public class TicketCheckInNotAllowedException extends RuntimeException {
    public TicketCheckInNotAllowedException(String message) {
        super(message);
    }
}
