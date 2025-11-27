package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class InvalidFlightScheduleException extends RuntimeException {
    public InvalidFlightScheduleException(String message) {
        super(message);
    }
}
