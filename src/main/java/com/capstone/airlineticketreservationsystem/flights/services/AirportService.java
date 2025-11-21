package com.capstone.airlineticketreservationsystem.flights.services;

import com.capstone.airlineticketreservationsystem.flights.dtos.AirportDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAirportRequest;
import com.capstone.airlineticketreservationsystem.flights.models.Airport;
import org.springframework.stereotype.Service;

import com.capstone.airlineticketreservationsystem.flights.repositories.AirportRepositoryDAO;

@Service
public class AirportService {
    public AirportService(AirportRepositoryDAO airportRepository) {
        this.airportRepository = airportRepository;
    }

    private final AirportRepositoryDAO airportRepository;

    public AirportDTO createAirport(CreateAirportRequest createAirportRequest) {

        Airport airport = new Airport(
                createAirportRequest.getAirportCode(),
                createAirportRequest.getAirportName(),
                createAirportRequest.getCity(),
                createAirportRequest.getCountry(),
                createAirportRequest.getTimezone()
        );

        Airport savedAirport = airportRepository.save(airport);

        return new AirportDTO(
                savedAirport.getAirportUUID(),
                savedAirport.getAirportCode(),
                savedAirport.getAirportName(),
                savedAirport.getCity(),
                savedAirport.getCountry(),
                savedAirport.getTimezone(),
                savedAirport.getCreatedAt()
        );
    }

}
