package com.capstone.airlineticketreservationsystem.flights.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
