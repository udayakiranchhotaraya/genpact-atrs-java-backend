package com.capstone.airlineticketreservationsystem.authentication.exceptions;

public class AccountDeactivatedException extends RuntimeException {
    public AccountDeactivatedException(String message) {
        super(message);
    }
}
