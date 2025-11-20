package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.util.List;
import java.util.Optional;

import com.capstone.airlineticketreservationsystem.flights.models.Airline;

public interface AirlineRepositoryDAO {
    Airline save(Airline airline);
    public List<Airline> findAll();
    public Optional<Airline> findByAirlineUUID(String airlineUuid);
    public Optional<Airline> findByAirlineCode(String airlineCode);
    public Airline update(Airline airline);
    public boolean existsByAirlineCode(String airlineCode);
}
