package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class AircraftTypeAlreadyDeletedException extends RuntimeException {
    public AircraftTypeAlreadyDeletedException(String message) {
        super(message);
    }
}
