package com.capstone.airlineticketreservationsystem.bookings.repositories;

import com.capstone.airlineticketreservationsystem.bookings.models.Refund;

import java.util.List;
import java.util.Optional;

public interface RefundRepositoryDAO {

    Refund save(Refund refund);

    Optional<Refund> findByUUID(String refundsUUID);

    List<Refund> findByBookingId(Long bookingId);

    List<Refund> findAll();

    Refund update(Refund refund);

    int softDelete(String refundsUUID);
}
