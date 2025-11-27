package com.capstone.airlineticketreservationsystem.flights.dtos;

public class FlightSearchCriteria {

    public FlightSearchCriteria() {
    }

    public FlightSearchCriteria(String departureAirportUUID, String arrivalAirportUUID, String airlineUUID, String seatType, String sortBy, String direction) {
        this.departureAirportUUID = departureAirportUUID;
        this.arrivalAirportUUID = arrivalAirportUUID;
        this.airlineUUID = airlineUUID;
        this.seatType = seatType;
        this.sortBy = sortBy;
        this.direction = direction;
    }

    private String departureAirportUUID;
    private String arrivalAirportUUID;
    private String airlineUUID;
    private String seatType = "economy"; // Default to economy
    private String sortBy = "custom"; // Use "custom" for your specific sorting
    private String direction = "asc";

    public String getDepartureAirportUUID() {
        return departureAirportUUID;
    }

    public void setDepartureAirportUUID(String departureAirportUUID) {
        this.departureAirportUUID = departureAirportUUID;
    }

    public String getArrivalAirportUUID() {
        return arrivalAirportUUID;
    }

    public void setArrivalAirportUUID(String arrivalAirportUUID) {
        this.arrivalAirportUUID = arrivalAirportUUID;
    }

    public String getAirlineUUID() {
        return airlineUUID;
    }

    public void setAirlineUUID(String airlineUUID) {
        this.airlineUUID = airlineUUID;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = (seatType != null) ? seatType : "economy";
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
