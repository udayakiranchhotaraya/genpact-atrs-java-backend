package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class AirportAlreadyExistsException extends RuntimeException {
    public AirportAlreadyExistsException(String message) {
        super(message);
    }
}