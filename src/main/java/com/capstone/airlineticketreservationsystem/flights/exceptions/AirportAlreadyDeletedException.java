package com.capstone.airlineticketreservationsystem.flights.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AirportAlreadyDeletedException extends RuntimeException {
    public AirportAlreadyDeletedException(String message) {
        super(message);
    }
}
