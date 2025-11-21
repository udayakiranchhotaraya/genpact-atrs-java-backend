package com.capstone.airlineticketreservationsystem.flights.dtos;

import jakarta.validation.constraints.Size;

public class UpdateAirportRequest {

    @Size(max = 100, message = "Airport name cannot exceed 100 characters")
    private String airportName;

    @Size(max = 100, message = "City name cannot exceed 100 characters")
    private String city;

    @Size(max = 100, message = "Country name cannot exceed 100 characters")
    private String country;

    @Size(max = 50, message = "Timezone cannot exceed 50 characters")
    private String timezone;

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
