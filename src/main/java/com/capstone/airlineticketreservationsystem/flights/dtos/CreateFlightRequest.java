package com.capstone.airlineticketreservationsystem.flights.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.capstone.airlineticketreservationsystem.flights.models.FlightStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateFlightRequest {

    @NotBlank(message = "Airline UUID is required")
    private String airlineUUID;

    @NotBlank(message = "Aircraft type UUID is required")
    private String aircraftTypeUUID;

    @NotBlank(message = "Flight number is required")
    private String flightNumber;

    @NotBlank(message = "Departure airport UUID is required")
    private String departureAirportUUID;

    @NotBlank(message = "Arrival airport UUID is required")
    private String arrivalAirportUUID;

    @NotNull(message = "Scheduled departure is required")
    private LocalDateTime scheduledDeparture;

    @NotNull(message = "Scheduled arrival is required")
    private LocalDateTime scheduledArrival;

    private FlightStatus status = FlightStatus.SCHEDULED;

    @NotNull(message = "Base economy price is required")
    @Positive(message = "Base economy price must be positive")
    private BigDecimal baseEconomyPrice;

    @NotNull(message = "Base business price is required")
    @Positive(message = "Base business price must be positive")
    private BigDecimal baseBusinessPrice;

    public String getAirlineUUID() {
        return airlineUUID;
    }

    public void setAirlineUUID(String airlineUUID) {
        this.airlineUUID = airlineUUID;
    }

    public String getAircraftTypeUUID() {
        return aircraftTypeUUID;
    }

    public void setAircraftTypeUUID(String aircraftTypeUUID) {
        this.aircraftTypeUUID = aircraftTypeUUID;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getDepartureAirportUUID() {
        return departureAirportUUID;
    }

    public void setDepartureAirportUUID(String departureAirportUUID) {
        this.departureAirportUUID = departureAirportUUID;
    }

    public String getArrivalAirportUUID() {
        return arrivalAirportUUID;
    }

    public void setArrivalAirportUUID(String arrivalAirportUUID) {
        this.arrivalAirportUUID = arrivalAirportUUID;
    }

    public LocalDateTime getScheduledDeparture() {
        return scheduledDeparture;
    }

    public void setScheduledDeparture(LocalDateTime scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    public LocalDateTime getScheduledArrival() {
        return scheduledArrival;
    }

    public void setScheduledArrival(LocalDateTime scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public BigDecimal getBaseEconomyPrice() {
        return baseEconomyPrice;
    }

    public void setBaseEconomyPrice(BigDecimal baseEconomyPrice) {
        this.baseEconomyPrice = baseEconomyPrice;
    }

    public BigDecimal getBaseBusinessPrice() {
        return baseBusinessPrice;
    }

    public void setBaseBusinessPrice(BigDecimal baseBusinessPrice) {
        this.baseBusinessPrice = baseBusinessPrice;
    }
}
