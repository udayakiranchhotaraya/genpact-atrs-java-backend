package com.capstone.airlineticketreservationsystem.flights.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CreateAirportRequest {

    @NotBlank(message = "Airport code is required")
    @Pattern(regexp = "^[A-Z]{3}$", message = "Airport code must be exactly three uppercase letters (as per IATA standard).")
    private String airportCode;

    @NotBlank(message = "Airport name is required")
    @Size(max = 100, message = "Airport name cannot exceed 100 characters")
    private String airportName;

    @NotBlank(message = "City is required")
    @Size(max = 100, message = "City name cannot exceed 100 characters")
    private String city;

    @NotBlank(message = "Country is required")
    @Size(max = 100, message = "Country name cannot exceed 100 characters")
    private String country;

    @NotBlank(message = "Timezone is required")
    @Size(max = 50, message = "Timezone cannot exceed 50 characters")
    private String timezone;

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
}
