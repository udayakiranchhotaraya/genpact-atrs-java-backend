package com.capstone.airlineticketreservationsystem.flights.controllers;

import com.capstone.airlineticketreservationsystem.flights.dtos.CreateFlightRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.FlightDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.FlightSearchCriteria;
import com.capstone.airlineticketreservationsystem.flights.dtos.FlightSearchRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.UpdateFlightRequest;
import com.capstone.airlineticketreservationsystem.flights.services.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping(value = "/search")
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

    @PutMapping(value = "/{flightUUID}")
    public ResponseEntity<FlightDTO> updateFlight(
            @PathVariable String flightUUID,
            @Valid @RequestBody UpdateFlightRequest updateFlightRequest) {

        FlightDTO updatedFlight = flightService.updateFlight(flightUUID, updateFlightRequest);
        return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
    }

    @DeleteMapping("/{flightUUID}")
    public ResponseEntity<Map<String, String>> deleteFlight(@PathVariable String flightUUID) {
        flightService.deleteFlightByUUID(flightUUID);

        Map<String, String> response = new LinkedHashMap<>();
        response.put("message", "Flight deleted successfully");
        response.put("flightUUID", flightUUID);
        response.put("timestamp", Instant.now().toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Flights API is healthy", HttpStatus.OK);
    }
    
    @PostMapping("/search")
    public ResponseEntity<List<FlightDTO>> searchFlights(
            @Valid @RequestBody FlightSearchRequest request
    ) {
        return ResponseEntity.ok(flightService.searchFlights(request));
    }

}
