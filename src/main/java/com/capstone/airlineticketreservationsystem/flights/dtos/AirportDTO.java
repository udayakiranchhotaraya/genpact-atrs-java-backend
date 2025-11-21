package com.capstone.airlineticketreservationsystem.flights.dtos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AirportDTO {

    public AirportDTO() {
    }

    public AirportDTO(String airportUUID, String airportCode, String airportName, String city, String country, String timezone, LocalDateTime createdAt) {
        this.airportUUID = airportUUID;
        this.airportCode = airportCode;
        this.airportName = airportName;
        this.city = city;
        this.country = country;
        this.timezone = timezone;
        this.createdAt = createdAt;
    }

    public AirportDTO(String airportUUID, String airportCode, String airportName, String city, String country, String timezone, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.airportUUID = airportUUID;
        this.airportCode = airportCode;
        this.airportName = airportName;
        this.city = city;
        this.country = country;
        this.timezone = timezone;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private String airportUUID;
    private String airportCode;
    private String airportName;
    private String city;
    private String country;
    private String timezone;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public String getAirportUUID() {
        return airportUUID;
    }

    public void setAirportUUID(String airportUUID) {
        this.airportUUID = airportUUID;
    }

    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
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
