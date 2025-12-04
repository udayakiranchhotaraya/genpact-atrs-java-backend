package com.capstone.airlineticketreservationsystem.flights.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // For semantic/business logic errors (e.g., arrival before departure, past schedule date)
public class InvalidFlightScheduleException extends RuntimeException {
    public InvalidFlightScheduleException(String message) {
        super(message);
    }
}
