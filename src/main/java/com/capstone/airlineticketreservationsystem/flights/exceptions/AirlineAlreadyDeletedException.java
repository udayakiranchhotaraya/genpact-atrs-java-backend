package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class AirlineAlreadyDeletedException extends RuntimeException {
    public AirlineAlreadyDeletedException(String message) {
        super(message);
    }
}
