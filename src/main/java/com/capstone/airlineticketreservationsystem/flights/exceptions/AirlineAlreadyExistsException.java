package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class AirlineAlreadyExistsException extends RuntimeException {
    public AirlineAlreadyExistsException(String message) {
        super(message);
    }
}
