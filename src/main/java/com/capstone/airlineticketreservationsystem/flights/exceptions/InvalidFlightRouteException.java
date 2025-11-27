package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class InvalidFlightRouteException extends RuntimeException {
    public InvalidFlightRouteException(String message) {
        super(message);
    }
}
