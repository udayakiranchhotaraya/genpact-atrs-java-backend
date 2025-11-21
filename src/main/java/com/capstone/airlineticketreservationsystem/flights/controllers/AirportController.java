package com.capstone.airlineticketreservationsystem.flights.controllers;

import com.capstone.airlineticketreservationsystem.flights.dtos.AirportDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.CreateAirportRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.UpdateAirportRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capstone.airlineticketreservationsystem.flights.services.AirportService;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/{airportUUID}")
    public ResponseEntity<AirportDTO> getAirportByUUID(@PathVariable String airportUUID) {
        AirportDTO airport = airportService.getAirportByUUID(airportUUID);
        return new ResponseEntity<>(airport, HttpStatus.OK);
    }

    @GetMapping(value = "/code/{airportCode}")
    public ResponseEntity<AirportDTO> getAirportByCode(@PathVariable String airportCode) {
        AirportDTO airport = airportService.getAirportByCode(airportCode);
        return new ResponseEntity<>(airport, HttpStatus.OK);
    }

    // Filtered search endpoints
    @GetMapping(value = "/search/name")
    public ResponseEntity<List<AirportDTO>> searchAirportsByName(@RequestParam String name) {
        List<AirportDTO> airports = airportService.searchAirportsByName(name);
        return new ResponseEntity<>(airports, HttpStatus.OK);
    }

    @GetMapping(value = "/search/city")
    public ResponseEntity<List<AirportDTO>> searchAirportsByCity(@RequestParam String city) {
        List<AirportDTO> airports = airportService.searchAirportsByCity(city);
        return new ResponseEntity<>(airports, HttpStatus.OK);
    }

    @GetMapping(value = "/search")
    public ResponseEntity<List<AirportDTO>> searchAirports(@RequestParam String q) {
        List<AirportDTO> airports = airportService.searchAirports(q);
        return new ResponseEntity<>(airports, HttpStatus.OK);
    }

    @PutMapping(value = "/{airportUUID}")
    public ResponseEntity<AirportDTO> updateAirport(
            @PathVariable String airportUUID,
            @Valid @RequestBody UpdateAirportRequest request) {

        AirportDTO updatedAirport = airportService.updateAirport(airportUUID, request);
        return new ResponseEntity<>(updatedAirport, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{airportUUID}")
    public ResponseEntity<Map<String, String>> deleteAirport(@PathVariable String airportUUID) {

        airportService.deleteAirportByUUID(airportUUID);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", "Airport deleted successfully");
        response.put("airportUUID", airportUUID);
        response.put("timestamp", Instant.now().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
