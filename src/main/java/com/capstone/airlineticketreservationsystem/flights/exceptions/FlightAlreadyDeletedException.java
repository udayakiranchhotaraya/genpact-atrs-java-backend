package com.capstone.airlineticketreservationsystem.flights.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class FlightAlreadyDeletedException extends RuntimeException {
    public FlightAlreadyDeletedException(String message) {
        super(message);
    }
}
