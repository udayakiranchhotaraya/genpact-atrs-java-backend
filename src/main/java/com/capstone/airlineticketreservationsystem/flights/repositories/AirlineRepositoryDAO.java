package com.capstone.airlineticketreservationsystem.flights.repositories;

import com.capstone.airlineticketreservationsystem.flights.models.Airline;

public interface AirlineRepositoryDAO {
    Airline save(Airline airline);
}
