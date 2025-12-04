package com.capstone.airlineticketreservationsystem.bookings.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST) // General client error during payment creation
public class PaymentCreationException extends RuntimeException {
    public PaymentCreationException(String message) {
        super(message);
    }
}