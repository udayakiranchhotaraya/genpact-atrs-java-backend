package com.capstone.airlineticketreservationsystem.bookings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TicketCreationException extends RuntimeException {
    public TicketCreationException(String message) {
        super(message);
    }
}
