package com.capstone.airlineticketreservationsystem.flights.controllers;

import com.capstone.airlineticketreservationsystem.flights.dtos.CreateFlightRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.FlightDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.FlightSearchCriteria;
import com.capstone.airlineticketreservationsystem.flights.services.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightDTO> createFlight(@Valid @RequestBody CreateFlightRequest createFlightRequest) {
        FlightDTO createdFlight = flightService.createFlight(createFlightRequest);
        return new ResponseEntity<>(createdFlight, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<FlightDTO>> getAllFlights(Pageable pageable) {
        Page<FlightDTO> flights = flightService.getAllFlights(pageable);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping(value = "/{flightUUID}")
    public ResponseEntity<FlightDTO> getFlightByUUID(@PathVariable String flightUUID) {
        FlightDTO flight = flightService.getFlightByUUID(flightUUID);
        return new ResponseEntity<>(flight, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<FlightDTO>> searchFlights(
            @RequestParam(required = false) String departureAirportUUID,
            @RequestParam(required = false) String arrivalAirportUUID,
            @RequestParam(required = false) String airlineUUID,
            @RequestParam(defaultValue = "economy") String seatType, // New parameter
            @RequestParam(defaultValue = "custom") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        FlightSearchCriteria criteria = new FlightSearchCriteria(departureAirportUUID, arrivalAirportUUID, airlineUUID, seatType, sortBy, direction);

        Pageable pageable = PageRequest.of(page, size);
        Page<FlightDTO> results = flightService.searchFlights(criteria, pageable);

        return new ResponseEntity<>(results, HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Flights API is healthy", HttpStatus.OK);
    }
}
