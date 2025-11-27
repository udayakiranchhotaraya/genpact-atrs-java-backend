package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException(String message) {
        super(message);
    }
}
