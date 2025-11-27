package com.capstone.airlineticketreservationsystem.flights.dtos;

import com.capstone.airlineticketreservationsystem.flights.models.FlightStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class UpdateFlightRequest {
    private String flightNumber;
    private String airlineUUID;
    private String aircraftTypeUUID;
    private String departureAirportUUID;
    private String arrivalAirportUUID;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledDeparture;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledArrival;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualDeparture;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualArrival;

    private FlightStatus status;
    private BigDecimal baseEconomyPrice;
    private BigDecimal baseBusinessPrice;

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

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

    public LocalDateTime getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(LocalDateTime actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public LocalDateTime getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(LocalDateTime actualArrival) {
        this.actualArrival = actualArrival;
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
