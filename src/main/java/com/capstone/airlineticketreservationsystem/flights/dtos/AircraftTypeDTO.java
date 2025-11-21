package com.capstone.airlineticketreservationsystem.flights.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AircraftTypeDTO {

    public AircraftTypeDTO() {
    }

    public AircraftTypeDTO(String aircraftTypeUUID, String aircraftModel, String manufacturer, Integer totalSeats, Integer businessClassSeats, Integer economyClassSeats, LocalDateTime createdAt) {
        this.aircraftTypeUUID = aircraftTypeUUID;
        this.aircraftModel = aircraftModel;
        this.manufacturer = manufacturer;
        this.totalSeats = totalSeats;
        this.businessClassSeats = businessClassSeats;
        this.economyClassSeats = economyClassSeats;
        this.createdAt = createdAt;
    }

    public AircraftTypeDTO(String aircraftTypeUUID, String aircraftModel, String manufacturer, Integer totalSeats, Integer businessClassSeats, Integer economyClassSeats, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.aircraftTypeUUID = aircraftTypeUUID;
        this.aircraftModel = aircraftModel;
        this.manufacturer = manufacturer;
        this.totalSeats = totalSeats;
        this.businessClassSeats = businessClassSeats;
        this.economyClassSeats = economyClassSeats;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private String aircraftTypeUUID;
    private String aircraftModel;
    private String manufacturer;
    private Integer totalSeats;
    private Integer businessClassSeats;
    private Integer economyClassSeats;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public String getAircraftTypeUUID() {
        return aircraftTypeUUID;
    }

    public void setAircraftTypeUUID(String aircraftTypeUUID) {
        this.aircraftTypeUUID = aircraftTypeUUID;
    }

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
