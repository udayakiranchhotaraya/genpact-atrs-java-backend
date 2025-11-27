package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.util.List;
import java.util.Optional;

import com.capstone.airlineticketreservationsystem.flights.models.AircraftType;

public interface AircraftTypeRepositoryDAO {
    public AircraftType save(AircraftType aircraftType);
    public List<AircraftType> findAll();
    public Optional<AircraftType> findByAircraftTypeUUID(String aircraftTypeUUID);
    public AircraftType update(AircraftType aircraftType);
    public int softDeleteByUUID(String aircraftTypeUUID);
    public boolean existsByUUIDAndNotDeleted(String aircraftTypeUUID);
    Optional<Long> findIdByUUID(String aircraftTypeUUID);
    Optional<AircraftType> findById(Long id);
}
