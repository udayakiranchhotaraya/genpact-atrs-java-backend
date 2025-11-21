package com.capstone.airlineticketreservationsystem.flights.controllers;

import com.capstone.airlineticketreservationsystem.flights.dtos.AirportDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAirportRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capstone.airlineticketreservationsystem.flights.services.AirportService;

@RestController
@RequestMapping("/api/airports")
public class AirportController {

    public AirportController(AirportService airportService) {
        this.airportService = airportService;
    }

    private final AirportService airportService;

    @PostMapping
    public ResponseEntity<AirportDTO> createAirport(@Valid @RequestBody CreateAirportRequest request) {
        AirportDTO createdAirport = airportService.createAirport(request);
        return new ResponseEntity<>(createdAirport, HttpStatus.CREATED);
    }
}
