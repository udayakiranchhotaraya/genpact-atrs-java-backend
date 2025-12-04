package com.capstone.airlineticketreservationsystem.flights.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AircraftTypeNotFoundException extends RuntimeException {
    public AircraftTypeNotFoundException(String message) {
        super(message);
    }
}
