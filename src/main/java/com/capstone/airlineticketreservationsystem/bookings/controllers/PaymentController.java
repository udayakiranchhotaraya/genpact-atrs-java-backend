package com.capstone.airlineticketreservationsystem.bookings.controllers;

import com.capstone.airlineticketreservationsystem.bookings.dtos.CreatePaymentRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.PaymentDTO;
import com.capstone.airlineticketreservationsystem.bookings.services.PaymentService;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    // CREATE PAYMENT
    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody CreatePaymentRequest req) {
        return ResponseEntity.ok(paymentService.createPayment(req));
    }

    // GET PAYMENT BY UUID
    @GetMapping("/{paymentUUID}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable String paymentUUID) {
        return ResponseEntity.ok(paymentService.getPayment(paymentUUID));
    }

    // GET ALL PAYMENTS FOR BOOKING
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsForBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(paymentService.getPaymentsForBooking(bookingId));
    }
    
    @PostMapping("/confirm/{paymentUUID}")
    public ResponseEntity<String> confirmPayment(@PathVariable String paymentUUID) {
        paymentService.confirmPayment(paymentUUID);
        return ResponseEntity.ok("Payment confirmed and tickets generated successfully.");
    }

}
