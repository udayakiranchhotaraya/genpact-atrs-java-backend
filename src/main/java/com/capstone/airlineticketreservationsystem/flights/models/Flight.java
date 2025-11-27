package com.capstone.airlineticketreservationsystem.flights.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Flight {

    public Flight() {
    }

    public Flight(String flightUUID, Long airlineId, Long aircraftTypeId, String flightNumber, Long departureAirportId, Long arrivalAirportId, LocalDateTime scheduledDeparture, LocalDateTime scheduledArrival, LocalDateTime actualDeparture, LocalDateTime actualArrival, BigDecimal baseEconomyPrice, BigDecimal baseBusinessPrice) {
        this.flightUUID = flightUUID;
        this.airlineId = airlineId;
        this.aircraftTypeId = aircraftTypeId;
        this.flightNumber = flightNumber;
        this.departureAirportId = departureAirportId;
        this.arrivalAirportId = arrivalAirportId;
        this.scheduledDeparture = scheduledDeparture;
        this.scheduledArrival = scheduledArrival;
        this.actualDeparture = actualDeparture;
        this.actualArrival = actualArrival;
        this.status = FlightStatus.SCHEDULED;
        this.baseEconomyPrice = baseEconomyPrice;
        this.baseBusinessPrice = baseBusinessPrice;
    }

    private Long id;
    private String flightUUID;
    private Long airlineId;
    private Long aircraftTypeId;
    private String flightNumber;
    private Long departureAirportId;
    private Long arrivalAirportId;
    private LocalDateTime scheduledDeparture;
    private LocalDateTime scheduledArrival;
    private LocalDateTime actualDeparture;
    private LocalDateTime actualArrival;
    private FlightStatus status;
    private BigDecimal baseEconomyPrice;
    private BigDecimal baseBusinessPrice;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFlightUUID() {
        return flightUUID;
    }

    public void setFlightUUID(String flightUUID) {
        this.flightUUID = flightUUID;
    }

    public Long getAirlineId() {
        return airlineId;
    }

    public void setAirlineId(Long airlineId) {
        this.airlineId = airlineId;
    }

    public Long getAircraftTypeId() {
        return aircraftTypeId;
    }

    public void setAircraftTypeId(Long aircraftTypeId) {
        this.aircraftTypeId = aircraftTypeId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public Long getDepartureAirportId() {
        return departureAirportId;
    }

    public void setDepartureAirportId(Long departureAirportId) {
        this.departureAirportId = departureAirportId;
    }

    public Long getArrivalAirportId() {
        return arrivalAirportId;
    }

    public void setArrivalAirportId(Long arrivalAirportId) {
        this.arrivalAirportId = arrivalAirportId;
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

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}

