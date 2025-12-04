// For unexpected failures while saving a passenger — DB errors, null pointers, etc
package com.capstone.airlineticketreservationsystem.bookings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PassengerCreationException extends RuntimeException {
    public PassengerCreationException(String message) {
        super(message);
    }
}
