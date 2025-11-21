package com.capstone.airlineticketreservationsystem.flights.dtos;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class UpdateAircraftTypeRequest {

    @Size(max = 50, message = "Aircraft model cannot exceed 50 characters")
    private String aircraftModel;

    @Size(max = 50, message = "Manufacturer cannot exceed 50 characters")
    private String manufacturer;

    @Positive(message = "Total seats must be a positive number")
    private Integer totalSeats;

    @Positive(message = "Business class seats must be a positive number")
    private Integer businessClassSeats;

    @Positive(message = "Economy class seats must be a positive number")
    private Integer economyClassSeats;

    public String getAircraftModel() {
        return aircraftModel;
    }

    public void setAircraftModel(String aircraftModel) {
        this.aircraftModel = aircraftModel;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public Integer getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(Integer totalSeats) {
        this.totalSeats = totalSeats;
    }

    public Integer getBusinessClassSeats() {
        return businessClassSeats;
    }

    public void setBusinessClassSeats(Integer businessClassSeats) {
        this.businessClassSeats = businessClassSeats;
    }

    public Integer getEconomyClassSeats() {
        return economyClassSeats;
    }

    public void setEconomyClassSeats(Integer economyClassSeats) {
        this.economyClassSeats = economyClassSeats;
    }
}
