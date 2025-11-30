package com.capstone.airlineticketreservationsystem.bookings.exceptions;

public class RefundNotFoundException extends RuntimeException {
    public RefundNotFoundException(String msg) {
        super(msg);
    }
}
