package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.util.Optional;

import com.capstone.airlineticketreservationsystem.flights.models.Flight;

public interface FlightRepositoryDAO {

    Flight save(Flight flight);
    Optional<Flight> findByFlightUUID(String flightUUID);
}
