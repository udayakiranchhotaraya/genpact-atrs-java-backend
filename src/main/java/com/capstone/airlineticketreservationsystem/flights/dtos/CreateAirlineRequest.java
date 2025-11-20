package com.capstone.airlineticketreservationsystem.flights.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateAirlineRequest {

    @NotBlank(message = "Airline code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Airline code must be exactly three uppercase letters (as per IATA standard).")
    private String airlineCode;

    @NotBlank(message = "Airline name is required")
    @Size(max = 100, message = "Airline name cannot exceed 100 characters")
    private String airlineName;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country name cannot exceed 100 characters")
    private String country;

    private String logoUrl;

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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
