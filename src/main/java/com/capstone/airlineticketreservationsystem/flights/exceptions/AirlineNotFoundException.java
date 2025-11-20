package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class AirlineNotFoundException extends RuntimeException {
    public AirlineNotFoundException(String message) {
        super(message);
    }
}
