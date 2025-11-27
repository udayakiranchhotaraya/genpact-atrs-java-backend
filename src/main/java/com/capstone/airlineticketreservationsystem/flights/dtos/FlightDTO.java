package com.capstone.airlineticketreservationsystem.flights.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import com.capstone.airlineticketreservationsystem.flights.models.FlightStatus;

public class FlightDTO {

    private String flightUUID;
    private String flightNumber;

    // Airline Details
    private String airlineUUID;
    private String airlineCode;
    private String airlineName;
    private String airlineCountry;
    private String airlineLogoUrl;

    // Aircraft Type Details
    private String aircraftTypeUUID;
    private String aircraftModel;
    private String manufacturer;
    private Integer totalSeats;
    private Integer businessClassSeats;
    private Integer economyClassSeats;

    // Airport Details
    private AirportInfo departureAirport;
    private AirportInfo arrivalAirport;

    // Flight Timing
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledDeparture;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime scheduledArrival;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualDeparture;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime actualArrival;

    private FlightStatus status;
    private BigDecimal baseEconomyPrice;
    private BigDecimal baseBusinessPrice;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public String getFlightUUID() {
        return flightUUID;
    }

    public void setFlightUUID(String flightUUID) {
        this.flightUUID = flightUUID;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

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

    public String getAirlineCountry() {
        return airlineCountry;
    }

    public void setAirlineCountry(String airlineCountry) {
        this.airlineCountry = airlineCountry;
    }

    public String getAirlineLogoUrl() {
        return airlineLogoUrl;
    }

    public void setAirlineLogoUrl(String airlineLogoUrl) {
        this.airlineLogoUrl = airlineLogoUrl;
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

    public AirportInfo getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(AirportInfo departureAirport) {
        this.departureAirport = departureAirport;
    }

    public AirportInfo getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(AirportInfo arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public LocalDateTime getScheduledDeparture() {
        return scheduledDeparture;
    }

    public void setScheduledDeparture(LocalDateTime scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    public LocalDateTime getScheduledArrival() {
        return scheduledArrival;
    }

    public void setScheduledArrival(LocalDateTime scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
    }

    public LocalDateTime getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(LocalDateTime actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    public LocalDateTime getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(LocalDateTime actualArrival) {
        this.actualArrival = actualArrival;
    }

    public FlightStatus getStatus() {
        return status;
    }

    public void setStatus(FlightStatus status) {
        this.status = status;
    }

    public BigDecimal getBaseEconomyPrice() {
        return baseEconomyPrice;
    }

    public void setBaseEconomyPrice(BigDecimal baseEconomyPrice) {
        this.baseEconomyPrice = baseEconomyPrice;
    }

    public BigDecimal getBaseBusinessPrice() {
        return baseBusinessPrice;
    }

    public void setBaseBusinessPrice(BigDecimal baseBusinessPrice) {
        this.baseBusinessPrice = baseBusinessPrice;
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

    public static class AirportInfo {

        public AirportInfo() {
        }

        private String airportUUID;
        private String airportCode;
        private String airportName;
        private String city;
        private String country;
        private String timezone;

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
    }
}
