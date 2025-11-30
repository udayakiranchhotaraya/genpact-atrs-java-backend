package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.time.LocalDate;
import java.util.List;
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
    int softDeleteByUUID(String flightUUID);
    boolean existsByUUIDAndNotDeleted(String flightUUID);
    Optional<Flight> findById(Long id);
    Optional<Long> findIdByUUID(String flightUUID);
    List<Flight> searchFlights(String origin, String destination, LocalDate date);
    
    int reduceSeatCount(Long flightId, String seatClass, int count);
    int increaseSeatCount(Long flightId, String seatClass, int count);


}
