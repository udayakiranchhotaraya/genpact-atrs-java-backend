package com.capstone.airlineticketreservationsystem.flights.dtos;

import jakarta.validation.constraints.Size;

public class UpdateAirlineRequest {

    @Size(max = 100, message = "Airline name cannot exceed 100 characters")
    private String airlineName;

    @Size(max = 100, message = "Country name cannot exceed 100 characters")
    private String country;

    @Size(max = 500, message = "Logo URL cannot exceed 500 characters")
    private String logoUrl;

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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
