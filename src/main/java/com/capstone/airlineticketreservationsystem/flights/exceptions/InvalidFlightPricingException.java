package com.capstone.airlineticketreservationsystem.flights.exceptions;

public class InvalidFlightPricingException extends RuntimeException {
    public InvalidFlightPricingException(String message) {
        super(message);
    }
}
