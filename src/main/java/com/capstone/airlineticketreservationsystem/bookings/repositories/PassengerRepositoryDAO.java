package com.capstone.airlineticketreservationsystem.bookings.repositories;

import com.capstone.airlineticketreservationsystem.bookings.models.Passenger;

import java.util.List;
import java.util.Optional;

public interface PassengerRepositoryDAO {

    Passenger save(Passenger passenger);

    Optional<Passenger> findByUUID(String passengersUUID);

    List<Passenger> findByBookingId(Long bookingId);
    Optional<Long> findIdByUUID(String passengerUUID);

    Passenger update(Passenger passenger);

    int softDelete(String passengersUUID);
}
