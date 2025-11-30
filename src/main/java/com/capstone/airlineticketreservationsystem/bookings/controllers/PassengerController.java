package com.capstone.airlineticketreservationsystem.bookings.controllers;

import com.capstone.airlineticketreservationsystem.bookings.dtos.CreatePassengerRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.PassengerDTO;
import com.capstone.airlineticketreservationsystem.bookings.services.PassengerService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    // CREATE PASSENGER
    @PostMapping
    public ResponseEntity<PassengerDTO> create(@Valid @RequestBody CreatePassengerRequest request) {
        return ResponseEntity.ok(passengerService.createPassenger(request));
    }

    // GET PASSENGER BY UUID
    @GetMapping("/{passengersUUID}")
    public ResponseEntity<PassengerDTO> getByUUID(@PathVariable String passengersUUID) {
        return ResponseEntity.ok(passengerService.getPassenger(passengersUUID));
    }

    // GET ALL PASSENGERS OF A BOOKING
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PassengerDTO>> getByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(passengerService.getPassengersByBooking(bookingId));
    }

    // UPDATE PASSENGER
    @PutMapping("/{passengersUUID}")
    public ResponseEntity<PassengerDTO> update(
            @PathVariable String passengersUUID,
            @Valid @RequestBody CreatePassengerRequest request
    ) {
        return ResponseEntity.ok(passengerService.updatePassenger(passengersUUID, request));
    }

    // SOFT DELETE PASSENGER
    @DeleteMapping("/{passengersUUID}")
    public ResponseEntity<String> delete(@PathVariable String passengersUUID) {
        passengerService.deletePassenger(passengersUUID);
        return ResponseEntity.ok("Passenger deleted successfully");
    }
}
