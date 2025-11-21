package com.capstone.airlineticketreservationsystem.flights.controllers;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capstone.airlineticketreservationsystem.flights.dtos.AircraftTypeDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAircraftTypeRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.UpdateAircraftTypeRequest;
import com.capstone.airlineticketreservationsystem.flights.services.AircraftTypeService;

@RestController
@RequestMapping("/api/aircraft-types")
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

    @PutMapping(value = "/{aircraftTypeUUID}")
    public ResponseEntity<AircraftTypeDTO> updateAircraftType(
            @PathVariable String aircraftTypeUUID,
            @Valid @RequestBody UpdateAircraftTypeRequest updateAircraftTypeRequest) {

        AircraftTypeDTO updatedAircraftType = aircraftTypeService.updateAircraftType(aircraftTypeUUID, updateAircraftTypeRequest);
        return new ResponseEntity<>(updatedAircraftType, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{aircraftTypeUUID}")
    public ResponseEntity<Map<String, String>> deleteAircraftType(@PathVariable String aircraftTypeUUID) {
        aircraftTypeService.deleteAircraftTypeByUUID(aircraftTypeUUID);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", "Aircraft type deleted successfully");
        response.put("aircraftTypeUUID", aircraftTypeUUID);
        response.put("timestamp", Instant.now().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
