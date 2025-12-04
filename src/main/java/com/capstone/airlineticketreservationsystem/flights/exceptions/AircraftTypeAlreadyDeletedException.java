package com.capstone.airlineticketreservationsystem.flights.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Request conflicts with the current (deleted) state of the resource
public class AircraftTypeAlreadyDeletedException extends RuntimeException {
    public AircraftTypeAlreadyDeletedException(String message) {
        super(message);
    }
}
