package com.capstone.airlineticketreservationsystem.flights.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class AirlineAlreadyExistsException extends RuntimeException {
    public AirlineAlreadyExistsException(String message) {
        super(message);
    }
}
