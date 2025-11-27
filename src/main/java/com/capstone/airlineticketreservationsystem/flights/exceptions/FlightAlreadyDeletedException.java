package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class FlightAlreadyDeletedException extends RuntimeException {
    public FlightAlreadyDeletedException(String message) {
        super(message);
    }
}
