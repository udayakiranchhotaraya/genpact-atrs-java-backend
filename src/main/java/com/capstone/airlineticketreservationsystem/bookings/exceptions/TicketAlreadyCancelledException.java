package com.capstone.airlineticketreservationsystem.bookings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // The requested operation conflicts with the current (cancelled) state of the ticket
public class TicketAlreadyCancelledException extends RuntimeException {
    public TicketAlreadyCancelledException(String message) {
        super(message);
    }
}
