package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.util.Map;
import java.util.Optional;

import com.capstone.airlineticketreservationsystem.flights.dtos.FlightDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.FlightSearchCriteria;
import com.capstone.airlineticketreservationsystem.flights.models.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlightRepositoryDAO {

    Flight save(Flight flight);
    Optional<Flight> findByFlightUUID(String flightUUID);
    Page<FlightDTO> findAll(Pageable pageable);
    public Page<FlightDTO> searchFlights(FlightSearchCriteria criteria, Pageable pageable);
    public Map<String, Integer> getSeatsAvailability(String flightUUID);
    Flight update(Flight flight);
}
