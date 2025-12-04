package com.capstone.airlineticketreservationsystem.flights.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // Fails business/validation rules (e.g., impossible airport connection, circular route)
public class InvalidFlightRouteException extends RuntimeException {
    public InvalidFlightRouteException(String message) {
        super(message);
    }
}
