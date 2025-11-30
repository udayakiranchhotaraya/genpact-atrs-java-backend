package com.capstone.airlineticketreservationsystem.bookings.controllers;

import com.capstone.airlineticketreservationsystem.bookings.dtos.CreateRefundRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.RefundDTO;
import com.capstone.airlineticketreservationsystem.bookings.services.RefundService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/refunds")
public class RefundController {

    private final RefundService refundService;

    public RefundController(RefundService refundService) {
        this.refundService = refundService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<RefundDTO> createRefund(@Valid @RequestBody CreateRefundRequest request) {
        return ResponseEntity.ok(refundService.createRefund(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{refundUUID}")
    public ResponseEntity<RefundDTO> getRefund(@PathVariable String refundUUID) {
        return ResponseEntity.ok(refundService.getRefundByUUID(refundUUID));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<RefundDTO>> getRefundsByBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(refundService.getRefundsByBooking(bookingId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{refundUUID}")
    public ResponseEntity<String> deleteRefund(@PathVariable String refundUUID) {
        refundService.deleteRefund(refundUUID);
        return ResponseEntity.ok("Refund deleted");
    }
}
