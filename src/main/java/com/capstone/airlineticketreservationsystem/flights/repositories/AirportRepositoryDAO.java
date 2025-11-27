package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.util.List;
import java.util.Optional;

import com.capstone.airlineticketreservationsystem.flights.models.Airport;

public interface AirportRepositoryDAO {
    public Airport save(Airport airport);
    public List<Airport> findAll();
    public Optional<Airport> findByAirportUUID(String airportUUID);
    public Optional<Airport> findByAirportCode(String airportCode);
    public List<Airport> findByAirportName(String name);
    public List<Airport> findByCity(String city);
    public List<Airport> searchAirports(String searchTerm);
    public Airport update(Airport airport);
    public int softDeleteByUUID(String airportUUID);
    public boolean existsByAirportCode(String airportCode);
    public boolean existsByUUIDAndNotDeleted(String airportUUID);
    Optional<Long> findIdByUUID(String airportUUID);
    Optional<Airport> findById(Long id);
}
