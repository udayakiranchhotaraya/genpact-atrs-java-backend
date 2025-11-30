package com.capstone.airlineticketreservationsystem.bookings.exceptions;

public class TicketAlreadyCancelledException extends RuntimeException {
    public TicketAlreadyCancelledException(String message) {
        super(message);
    }
}
