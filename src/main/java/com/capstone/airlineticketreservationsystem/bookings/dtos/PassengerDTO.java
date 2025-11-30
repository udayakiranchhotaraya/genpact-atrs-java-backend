package com.capstone.airlineticketreservationsystem.bookings.dtos;

import com.capstone.airlineticketreservationsystem.bookings.models.PassengerType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class PassengerDTO {

    private String passengersUUID;
    private Long bookingId;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private String passportNumber;
    private String nationality;
    private PassengerType passengerType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public String getPassengersUUID() { return passengersUUID; }
    public void setPassengersUUID(String passengersUUID) { this.passengersUUID = passengersUUID; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getPassportNumber() { return passportNumber; }
    public void setPassportNumber(String passportNumber) { this.passportNumber = passportNumber; }

    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }

    public PassengerType getPassengerType() { return passengerType; }
    public void setPassengerType(PassengerType passengerType) { this.passengerType = passengerType; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
