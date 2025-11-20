package com.capstone.airlineticketreservationsystem.flights.services;

import java.util.List;
import java.util.stream.Collectors;

import com.capstone.airlineticketreservationsystem.flights.exceptions.AirlineAlreadyExistsException;
import org.springframework.stereotype.Service;

import com.capstone.airlineticketreservationsystem.flights.dtos.AirlineDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAirlineRequest;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AirlineNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.models.Airline;
import com.capstone.airlineticketreservationsystem.flights.repositories.AirlineRepositoryDAO;

@Service
public class AirlineService {

    public AirlineService(AirlineRepositoryDAO airlineRepositoryDAO) {
        this.airlineRepositoryDAO = airlineRepositoryDAO;
    }

    public AirlineRepositoryDAO airlineRepositoryDAO;

    public AirlineDTO createAirline(CreateAirlineRequest airlineRequest) {

        if (airlineRepositoryDAO.existsByAirlineCode(airlineRequest.getAirlineCode())) {
            throw new AirlineAlreadyExistsException("Airline with code " + airlineRequest.getAirlineCode() + " already exists");
        }

        Airline airline = new Airline(
                airlineRequest.getAirlineCode(),
                airlineRequest.getAirlineName(),
                airlineRequest.getCountry(),
                airlineRequest.getLogoUrl()
        );

        Airline savedAirline = airlineRepositoryDAO.save(airline);

        return new AirlineDTO(
                savedAirline.getAirlineUUID(),
                savedAirline.getAirlineCode(),
                savedAirline.getAirlineName(),
                savedAirline.getCountry(),
                savedAirline.getLogoURL(),
                savedAirline.getCreatedAt()
        );
    }

    public List<AirlineDTO> getAllAirlines() {
        List<Airline> airlines = airlineRepositoryDAO.findAll();
        return airlines.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // Get airline by UUID
    public AirlineDTO getAirlineByUUID(String airlineUUID) {
        Airline airline = airlineRepositoryDAO.findByAirlineUUID(airlineUUID)
                .orElseThrow(() -> new AirlineNotFoundException(
                        "Airline not found with UUID: " + airlineUUID));
        return convertToDTO(airline);
    }

    // Get airline by code
    public AirlineDTO getAirlineByCode(String airlineCode) {
        Airline airline = airlineRepositoryDAO.findByAirlineCode(airlineCode)
                .orElseThrow(() -> new AirlineNotFoundException(
                        "Airline not found with code: " + airlineCode));
        return convertToDTO(airline);
    }

    private AirlineDTO convertToDTO(Airline airline) {
        return new AirlineDTO(
                airline.getAirlineUUID(),
                airline.getAirlineCode(),
                airline.getAirlineName(),
                airline.getCountry(),
                airline.getLogoURL(),
                airline.getCreatedAt(),
                airline.getUpdatedAt()
        );
    }
}
