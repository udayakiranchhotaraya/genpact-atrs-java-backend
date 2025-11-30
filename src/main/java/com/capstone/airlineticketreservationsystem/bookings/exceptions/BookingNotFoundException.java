package com.capstone.airlineticketreservationsystem.bookings.exceptions;

public class BookingNotFoundException extends RuntimeException {
    public BookingNotFoundException(String msg) { super(msg); }
}
