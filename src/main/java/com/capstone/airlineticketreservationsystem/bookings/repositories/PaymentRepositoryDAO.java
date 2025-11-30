package com.capstone.airlineticketreservationsystem.bookings.repositories;

import com.capstone.airlineticketreservationsystem.bookings.models.Payment;
import java.util.List;
import java.util.Optional;

public interface PaymentRepositoryDAO {

    Payment save(Payment payment);

    Optional<Payment> findByUUID(String paymentsUUID);

    Optional<Payment> findById(Long id);

    List<Payment> findByBookingId(Long bookingId);

    Payment update(Payment payment);

    int softDeleteByUUID(String paymentsUUID);
}
