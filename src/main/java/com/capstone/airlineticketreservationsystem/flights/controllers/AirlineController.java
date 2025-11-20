package com.capstone.airlineticketreservationsystem.flights.controllers;

import java.util.List;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capstone.airlineticketreservationsystem.flights.dtos.AirlineDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAirlineRequest;
import com.capstone.airlineticketreservationsystem.flights.services.AirlineService;

@RestController
@RequestMapping("/api/airlines")
public class AirlineController {
    public AirlineController(AirlineService airlineService) {
        this.airlineService = airlineService;
    }

    private final AirlineService airlineService;

    @PostMapping
    public ResponseEntity<AirlineDTO> createAirline(@Valid @RequestBody CreateAirlineRequest airlineRequest) {
        AirlineDTO createdAirline = airlineService.createAirline(airlineRequest);
        return new ResponseEntity<>(createdAirline, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<AirlineDTO>> getAllAirlines() {
        List<AirlineDTO> airlines = airlineService.getAllAirlines();
        return new ResponseEntity<>(airlines, HttpStatus.OK);
    }

    // Get airline by UUID
    @GetMapping(value = "/{airlineUuid}")
    public ResponseEntity<AirlineDTO> getAirlineByUUID(@PathVariable String airlineUUID) {
        AirlineDTO airline = airlineService.getAirlineByUUID(airlineUUID);
        return new ResponseEntity<>(airline, HttpStatus.OK);
    }

    // Get airline by code
    @GetMapping(value = "/code/{airlineCode}")
    public ResponseEntity<AirlineDTO> getAirlineByCode(@PathVariable String airlineCode) {
        AirlineDTO airline = airlineService.getAirlineByCode(airlineCode);
        return new ResponseEntity<>(airline, HttpStatus.OK);
    }
}
