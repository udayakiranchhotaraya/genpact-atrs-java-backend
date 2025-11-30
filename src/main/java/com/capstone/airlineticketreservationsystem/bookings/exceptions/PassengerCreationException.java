// For unexpected failures while saving a passenger — DB errors, null pointers, etc
package com.capstone.airlineticketreservationsystem.bookings.exceptions;

public class PassengerCreationException extends RuntimeException {
    public PassengerCreationException(String message) {
        super(message);
    }
}
