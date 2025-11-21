package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class AircraftTypeNotFoundException extends RuntimeException {
    public AircraftTypeNotFoundException(String message) {
        super(message);
    }
}
