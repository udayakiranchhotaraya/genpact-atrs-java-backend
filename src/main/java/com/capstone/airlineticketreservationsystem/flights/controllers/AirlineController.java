package com.capstone.airlineticketreservationsystem.flights.controllers;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.capstone.airlineticketreservationsystem.flights.dtos.AirlineDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAirlineRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.UpdateAirlineRequest;
import com.capstone.airlineticketreservationsystem.flights.services.AirlineService;

@RestController
@RequestMapping("/api/airlines")
public class AirlineController {
    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    private final AirlineService airlineService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<AirlineDTO> createAirline(@Valid @RequestBody CreateAirlineRequest airlineRequest) {
        AirlineDTO createdAirline = airlineService.createAirline(airlineRequest);
        return new ResponseEntity<>(createdAirline, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AirlineDTO>> getAllAirlines() {
        List<AirlineDTO> airlines = airlineService.getAllAirlines();
        return new ResponseEntity<>(airlines, HttpStatus.OK);
    }

    // Get airline by UUID
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/{airlineUUID}")
    public ResponseEntity<AirlineDTO> getAirlineByUUID(@PathVariable String airlineUUID) {
        AirlineDTO airline = airlineService.getAirlineByUUID(airlineUUID);
        return new ResponseEntity<>(airline, HttpStatus.OK);
    }

    // Get airline by code
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/code/{airlineCode}")
    public ResponseEntity<AirlineDTO> getAirlineByCode(@PathVariable String airlineCode) {
        AirlineDTO airline = airlineService.getAirlineByCode(airlineCode);
        return new ResponseEntity<>(airline, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{airlineUUID}")
    public ResponseEntity<AirlineDTO> updateAirline(
            @PathVariable String airlineUUID,
            @Valid @RequestBody UpdateAirlineRequest updateAirlineRequest) {

        AirlineDTO updatedAirline = airlineService.updateAirline(airlineUUID, updateAirlineRequest);
        return new ResponseEntity<>(updatedAirline, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping(value = "/{airlineUUID}")
    public ResponseEntity<Map<String, String>> deleteAirline(@PathVariable String airlineUUID) {
        airlineService.deleteAirline(airlineUUID);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Airline deleted successfully");
        response.put("airlineUuid", airlineUUID);
        response.put("timestamp", Instant.now().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
