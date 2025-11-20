package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.util.List;
import java.util.Optional;

import com.capstone.airlineticketreservationsystem.flights.models.AircraftType;

public interface AircraftTypeRepositoryDAO {
    public AircraftType save(AircraftType aircraftType);
    public List<AircraftType> findAll();
    public Optional<AircraftType> findByAircraftTypeUUID(String aircraftTypeUUID);
}
