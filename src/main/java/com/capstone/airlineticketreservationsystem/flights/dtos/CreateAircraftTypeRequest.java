package com.capstone.airlineticketreservationsystem.flights.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CreateAircraftTypeRequest {

    @NotBlank(message = "Aircraft model is required")
    private String aircraftModel;

    @NotBlank(message = "Manufacturer is required")
    private String manufacturer;

    @NotNull(message = "Total seats is required")
    @Positive(message = "Total seats must be a positive number")
    private Integer totalSeats;

    @NotNull(message = "Business class seats is required")
    @Positive(message = "Business class seats must be a positive number")
    private Integer businessClassSeats;

    @NotNull(message = "Economy class seats is required")
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
