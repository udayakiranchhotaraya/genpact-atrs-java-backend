package com.capstone.airlineticketreservationsystem.flights.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.capstone.airlineticketreservationsystem.flights.dtos.AircraftTypeDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAircraftTypeRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.UpdateAircraftTypeRequest;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AircraftTypeAlreadyDeletedException;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AircraftTypeNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.models.AircraftType;
import com.capstone.airlineticketreservationsystem.flights.repositories.AircraftTypeRepositoryDAO;

@Service
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
                savedAircraftType.getAircraftTypeUUID(),
                savedAircraftType.getAircraftModel(),
                savedAircraftType.getManufacturer(),
                savedAircraftType.getTotalSeats(),
                savedAircraftType.getBusinessClassSeats(),
                savedAircraftType.getEconomyClassSeats(),
                savedAircraftType.getCreatedAt()
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
                .orElseThrow(() -> new AircraftTypeNotFoundException("Aircraft type not found with UUID: " + aircraftTypeUUID));
        return convertToDTO(aircraftType);
    }

    public AircraftTypeDTO updateAircraftType(String aircraftTypeUUID, UpdateAircraftTypeRequest updateAircraftTypeRequest) {

        AircraftType existingAircraftType = aircraftTypeRepository.findByAircraftTypeUUID(aircraftTypeUUID)
                .orElseThrow(() -> new AircraftTypeNotFoundException("Aircraft type not found with UUID: " + aircraftTypeUUID));

        if (updateAircraftTypeRequest.getAircraftModel() != null) {
            existingAircraftType.setAircraftModel(updateAircraftTypeRequest.getAircraftModel());
        }
        if (updateAircraftTypeRequest.getManufacturer() != null) {
            existingAircraftType.setManufacturer(updateAircraftTypeRequest.getManufacturer());
        }
        if (updateAircraftTypeRequest.getTotalSeats() != null) {
            existingAircraftType.setTotalSeats(updateAircraftTypeRequest.getTotalSeats());
        }
        if (updateAircraftTypeRequest.getBusinessClassSeats() != null) {
            existingAircraftType.setBusinessClassSeats(updateAircraftTypeRequest.getBusinessClassSeats());
        }
        if (updateAircraftTypeRequest.getEconomyClassSeats() != null) {
            existingAircraftType.setEconomyClassSeats(updateAircraftTypeRequest.getEconomyClassSeats());
        }

        AircraftType updatedAircraftType = aircraftTypeRepository.update(existingAircraftType);
        return convertToDTO(updatedAircraftType);
    }

    public void deleteAircraftTypeByUUID(String aircraftTypeUUID) {

        if (!aircraftTypeRepository.existsByUUIDAndNotDeleted(aircraftTypeUUID)) {
            throw new AircraftTypeNotFoundException("Aircraft type not found with UUID: " + aircraftTypeUUID);
        }

        int rowsAffected = aircraftTypeRepository.softDeleteByUUID(aircraftTypeUUID);

        if (rowsAffected == 0) {
            throw new AircraftTypeAlreadyDeletedException("Aircraft type with UUID: " + aircraftTypeUUID + " is already deleted");
        }
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
