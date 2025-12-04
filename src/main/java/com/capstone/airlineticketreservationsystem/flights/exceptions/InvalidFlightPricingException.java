package com.capstone.airlineticketreservationsystem.flights.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY) // Fails business/validation rules (e.g., negative price, invalid calculation)
public class InvalidFlightPricingException extends RuntimeException {
    public InvalidFlightPricingException(String message) {
        super(message);
    }
}
