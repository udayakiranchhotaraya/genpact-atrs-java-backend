package com.capstone.airlineticketreservationsystem.flights.models;

import java.time.LocalDateTime;

public class AircraftType {

    public AircraftType() {
    }

    public AircraftType(String aircraftModel, String manufacturer, Integer totalSeats, Integer businessClassSeats, Integer economyClassSeats) {
        this.aircraftModel = aircraftModel;
        this.manufacturer = manufacturer;
        this.totalSeats = totalSeats;
        this.businessClassSeats = businessClassSeats;
        this.economyClassSeats = economyClassSeats;
    }

    private Long id;
    private String aircraftTypeUUID;
    private String aircraftModel;
    private String manufacturer;
    private Integer totalSeats;
    private Integer businessClassSeats;
    private Integer economyClassSeats;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
