package com.capstone.airlineticketreservationsystem.flights.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AirportAlreadyExistsException extends RuntimeException {
    public AirportAlreadyExistsException(String message) {
        super(message);
    }
}