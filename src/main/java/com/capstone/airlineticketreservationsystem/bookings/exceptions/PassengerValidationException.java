// For invalid data like DOB in the future, missing fields, wrong passport format, etc.
package com.capstone.airlineticketreservationsystem.bookings.exceptions;

public class PassengerValidationException extends RuntimeException {
    public PassengerValidationException(String message) {
        super(message);
    }
}
