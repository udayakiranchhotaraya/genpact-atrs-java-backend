// For invalid data like DOB in the future, missing fields, wrong passport format, etc.
package com.capstone.airlineticketreservationsystem.bookings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // HTTP 422 - The request was well-formed but contains semantic validation errors.
public class PassengerValidationException extends RuntimeException {
    public PassengerValidationException(String message) {
        super(message);
    }
}
