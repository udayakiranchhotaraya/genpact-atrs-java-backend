package com.capstone.airlineticketreservationsystem.bookings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // Request conflicts with the current (deleted) state of the resource
public class PassengerAlreadyDeletedException extends RuntimeException {
    public PassengerAlreadyDeletedException(String message) {
        super(message);
    }
}
