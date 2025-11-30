package com.capstone.airlineticketreservationsystem.bookings.dtos;

import com.capstone.airlineticketreservationsystem.bookings.models.SeatClass;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class CreateBookingRequest {

    @NotNull(message = "User UUID is required")
    private String userUUID;

    @NotNull(message = "Flight UUID is required")
    private String flightUUID;

    @NotNull(message = "Seat class is required")
    private SeatClass seatClass;  // ECONOMY / BUSINESS / FIRST

    @NotNull(message = "Passengers list cannot be empty")
    private List<PassengerRequest> passengers;


    // ----------------- GETTERS & SETTERS -----------------

    public String getUserUUID() {
        return userUUID;
    }

    public void setUserUUID(String userUUID) {
        this.userUUID = userUUID;
    }

    public String getFlightUUID() {
        return flightUUID;
    }

    public void setFlightUUID(String flightUUID) {
        this.flightUUID = flightUUID;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }

    public List<PassengerRequest> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerRequest> passengers) {
        this.passengers = passengers;
    }


    // ----------------- PASSENGER REQUEST -----------------

    public static class PassengerRequest {

        @NotNull(message = "First name is required")
        private String firstName;

        @NotNull(message = "Last name is required")
        private String lastName;

        private String passportNumber;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getPassportNumber() {
            return passportNumber;
        }

        public void setPassportNumber(String passportNumber) {
            this.passportNumber = passportNumber;
        }
    }
}
