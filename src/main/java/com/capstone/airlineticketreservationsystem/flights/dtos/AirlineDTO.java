package com.capstone.airlineticketreservationsystem.flights.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public class AirlineDTO {

    public AirlineDTO() {
    }

    public AirlineDTO(String airlineUUID, String airlineCode, String airlineName, String country, String logoURL, LocalDateTime createdAt) {
        this.airlineUUID = airlineUUID;
        this.airlineCode = airlineCode;
        this.airlineName = airlineName;
        this.country = country;
        this.logoURL = logoURL;
        this.createdAt = createdAt;
    }

    private String airlineUUID;
    private String airlineCode;
    private String airlineName;
    private String country;
    private String logoURL;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public String getAirlineUUID() {
        return airlineUUID;
    }

    public void setAirlineUUID(String airlineUUID) {
        this.airlineUUID = airlineUUID;
    }

    public String getAirlineCode() {
        return airlineCode;
    }

    public void setAirlineCode(String airlineCode) {
        this.airlineCode = airlineCode;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public void setAirlineName(String airlineName) {
        this.airlineName = airlineName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
