package com.capstone.airlineticketreservationsystem.bookings.controllers;

import com.capstone.airlineticketreservationsystem.bookings.dtos.BookingDTO;
import com.capstone.airlineticketreservationsystem.bookings.dtos.CreateBookingRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.UpdateBookingStatusRequest;
import com.capstone.airlineticketreservationsystem.bookings.services.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    private final BookingService bookingService;
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingDTO> createBooking(@Valid @RequestBody CreateBookingRequest request) {
        return new ResponseEntity<>(bookingService.createBooking(request), HttpStatus.CREATED);
    }

    @GetMapping("/{bookingUUID}")
    public ResponseEntity<BookingDTO> getBooking(@PathVariable String bookingUUID) {
        return ResponseEntity.ok(bookingService.getBooking(bookingUUID));
    }

    @GetMapping
    public ResponseEntity<List<BookingDTO>> getAll() {
        return ResponseEntity.ok(bookingService.getAllBookings());
    }

    @PutMapping("/{bookingUUID}/status")
    public ResponseEntity<BookingDTO> updateStatus(
            @PathVariable String bookingUUID,
            @Valid @RequestBody UpdateBookingStatusRequest request) {

        return ResponseEntity.ok(bookingService.updateStatus(bookingUUID, request));
    }

    @DeleteMapping("/{bookingUUID}")
    public ResponseEntity<Map<String,String>> delete(@PathVariable String bookingUUID) {
        bookingService.deleteBooking(bookingUUID);

        Map<String,String> map = new HashMap<>();
        map.put("message", "Booking deleted");
        map.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(map);
    }

    // ====== INITIATE BOOKING (same as create, but separate path for UI flow) ======
    @PostMapping("/initiate")
    public ResponseEntity<BookingDTO> initiateBooking(@Valid @RequestBody CreateBookingRequest req) {
        BookingDTO dto = bookingService.initiateBooking(req);
        return ResponseEntity.ok(dto);
    }
}
