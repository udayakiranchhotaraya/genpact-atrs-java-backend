package com.capstone.airlineticketreservationsystem.bookings.exceptions;

public class PassengerAlreadyDeletedException extends RuntimeException {
    public PassengerAlreadyDeletedException(String message) {
        super(message);
    }
}
