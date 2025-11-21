package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class AirportAlreadyDeletedException extends RuntimeException {
    public AirportAlreadyDeletedException(String message) {
        super(message);
    }
}
