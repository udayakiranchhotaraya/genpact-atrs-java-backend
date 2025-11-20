package com.capstone.airlineticketreservationsystem.flights.controllers;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.capstone.airlineticketreservationsystem.flights.dtos.AircraftTypeDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAircraftTypeRequest;
import com.capstone.airlineticketreservationsystem.flights.services.AircraftTypeService;

public class AircraftTypeController {

    public AircraftTypeController(AircraftTypeService aircraftTypeService) {
        this.aircraftTypeService = aircraftTypeService;
    }

    private final AircraftTypeService aircraftTypeService;

    @PostMapping
    public ResponseEntity<AircraftTypeDTO> createAircraftType(@Valid @RequestBody CreateAircraftTypeRequest createAircraftTypeRequest) {
        AircraftTypeDTO createdAircraftType = aircraftTypeService.createAircraftType(createAircraftTypeRequest);
        return new ResponseEntity<>(createdAircraftType, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AircraftTypeDTO>> getAllAircraftTypes() {
        List<AircraftTypeDTO> aircraftTypes = aircraftTypeService.getAllAircraftTypes();
        return new ResponseEntity<>(aircraftTypes, HttpStatus.OK);
    }

    @GetMapping(value = "/{aircraftTypeUUID}")
    public ResponseEntity<AircraftTypeDTO> getAircraftTypeByUUID(@PathVariable String aircraftTypeUUID) {
        AircraftTypeDTO aircraftType = aircraftTypeService.getAircraftTypeByUUID(aircraftTypeUUID);
        return new ResponseEntity<>(aircraftType, HttpStatus.OK);
    }
}
