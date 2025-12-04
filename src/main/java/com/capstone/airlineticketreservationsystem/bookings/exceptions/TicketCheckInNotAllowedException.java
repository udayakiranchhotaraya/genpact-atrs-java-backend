package com.capstone.airlineticketreservationsystem.bookings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // The client is authenticated but the action (check-in) is not alowed
public class TicketCheckInNotAllowedException extends RuntimeException {
    public TicketCheckInNotAllowedException(String message) {
        super(message);
    }
}
