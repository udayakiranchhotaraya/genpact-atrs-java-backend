package com.capstone.airlineticketreservationsystem.flights.services;

import org.springframework.stereotype.Service;

import com.capstone.airlineticketreservationsystem.flights.dtos.AirportDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAirportRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.UpdateAirportRequest;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AirportAlreadyDeletedException;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AirportAlreadyExistsException;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AirportNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.models.Airport;
import com.capstone.airlineticketreservationsystem.flights.repositories.AirportRepositoryDAO;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AirportService {
    public AirportService(AirportRepositoryDAO airportRepository) {
        this.airportRepository = airportRepository;
    }

    private final AirportRepositoryDAO airportRepository;

    public AirportDTO createAirport(CreateAirportRequest createAirportRequest) {

        if (airportRepository.existsByAirportCode(createAirportRequest.getAirportCode())) {
            throw new AirportAlreadyExistsException("Airport with code " + createAirportRequest.getAirportCode() + " already exists");
        }

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

    public List<AirportDTO> getAllAirports() {
        List<Airport> airports = airportRepository.findAll();
        return airports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AirportDTO getAirportByUUID(String airportUUID) {
        Airport airport = airportRepository.findByAirportUUID(airportUUID)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found with UUID: " + airportUUID));
        return convertToDTO(airport);
    }

    public AirportDTO getAirportByCode(String airportCode) {
        Airport airport = airportRepository.findByAirportCode(airportCode)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found with code: " + airportCode));
        return convertToDTO(airport);
    }

    public List<AirportDTO> searchAirportsByName(String name) {
        List<Airport> airports = airportRepository.findByAirportName(name);
        return airports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AirportDTO> searchAirportsByCity(String city) {
        List<Airport> airports = airportRepository.findByCity(city);
        return airports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AirportDTO> searchAirports(String searchTerm) {
        List<Airport> airports = airportRepository.searchAirports(searchTerm);
        return airports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AirportDTO updateAirport(String airportUUID, UpdateAirportRequest updateAirportRequest) {

        Airport existingAirport = airportRepository.findByAirportUUID(airportUUID)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found with UUID: " + airportUUID));

        if (updateAirportRequest.getAirportName() != null) {
            existingAirport.setAirportName(updateAirportRequest.getAirportName());
        }
        if (updateAirportRequest.getCity() != null) {
            existingAirport.setCity(updateAirportRequest.getCity());
        }
        if (updateAirportRequest.getCountry() != null) {
            existingAirport.setCountry(updateAirportRequest.getCountry());
        }
        if (updateAirportRequest.getTimezone() != null) {
            existingAirport.setTimezone(updateAirportRequest.getTimezone());
        }

        Airport updatedAirport = airportRepository.update(existingAirport);
        return convertToDTO(updatedAirport);
    }

    public void deleteAirportByUUID(String airportUUID) {

        if (!airportRepository.existsByUUIDAndNotDeleted(airportUUID)) {
            throw new AirportNotFoundException("Airport not found with UUID: " + airportUUID);
        }

        int rowsAffected = airportRepository.softDeleteByUUID(airportUUID);

        if (rowsAffected == 0) {
            throw new AirportAlreadyDeletedException("Airport with UUID: " + airportUUID + " is already deleted");
        }
    }

    private AirportDTO convertToDTO(Airport airport) {
        return new AirportDTO(
                airport.getAirportUUID(),
                airport.getAirportCode(),
                airport.getAirportName(),
                airport.getCity(),
                airport.getCountry(),
                airport.getTimezone(),
                airport.getCreatedAt(),
                airport.getUpdatedAt()
        );
    }
}
