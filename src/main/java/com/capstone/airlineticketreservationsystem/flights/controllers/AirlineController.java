package com.capstone.airlineticketreservationsystem.flights.controllers;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
