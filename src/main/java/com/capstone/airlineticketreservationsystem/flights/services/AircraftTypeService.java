package com.capstone.airlineticketreservationsystem.flights.services;

import com.capstone.airlineticketreservationsystem.flights.dtos.AircraftTypeDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAircraftTypeRequest;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AircraftTypeNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.models.AircraftType;
import com.capstone.airlineticketreservationsystem.flights.repositories.AircraftTypeRepositoryDAO;

import java.util.List;
import java.util.stream.Collectors;

public class AircraftTypeService {

    public AircraftTypeService(AircraftTypeRepositoryDAO aircraftTypeRepository) {
        this.aircraftTypeRepository = aircraftTypeRepository;
    }

    private final AircraftTypeRepositoryDAO aircraftTypeRepository;

    public AircraftTypeDTO createAircraftType(CreateAircraftTypeRequest createAircraftTypeRequest) {

        AircraftType aircraftType = new AircraftType(
                createAircraftTypeRequest.getAircraftModel(),
                createAircraftTypeRequest.getManufacturer(),
                createAircraftTypeRequest.getTotalSeats(),
                createAircraftTypeRequest.getBusinessClassSeats(),
                createAircraftTypeRequest.getEconomyClassSeats()
        );


        AircraftType savedAircraftType = aircraftTypeRepository.save(aircraftType);


        return new AircraftTypeDTO(
                aircraftType.getAircraftTypeUUID(),
                aircraftType.getAircraftModel(),
                aircraftType.getManufacturer(),
                aircraftType.getTotalSeats(),
                aircraftType.getBusinessClassSeats(),
                aircraftType.getEconomyClassSeats(),
                aircraftType.getCreatedAt()
        );
    }

    public List<AircraftTypeDTO> getAllAircraftTypes() {
        List<AircraftType> aircraftTypes = aircraftTypeRepository.findAll();
        return aircraftTypes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public AircraftTypeDTO getAircraftTypeByUUID(String aircraftTypeUUID) {
        AircraftType aircraftType = aircraftTypeRepository.findByAircraftTypeUUID(aircraftTypeUUID)
                .orElseThrow(() -> new AircraftTypeNotFoundException(
                        "Aircraft type not found with UUID: " + aircraftTypeUUID));
        return convertToDTO(aircraftType);
    }

    private AircraftTypeDTO convertToDTO(AircraftType aircraftType) {
        return new AircraftTypeDTO(
                aircraftType.getAircraftTypeUUID(),
                aircraftType.getAircraftModel(),
                aircraftType.getManufacturer(),
                aircraftType.getTotalSeats(),
                aircraftType.getBusinessClassSeats(),
                aircraftType.getEconomyClassSeats(),
                aircraftType.getCreatedAt(),
                aircraftType.getUpdatedAt()
        );
    }
}
