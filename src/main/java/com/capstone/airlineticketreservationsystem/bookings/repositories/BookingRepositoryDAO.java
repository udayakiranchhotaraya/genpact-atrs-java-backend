package com.capstone.airlineticketreservationsystem.bookings.repositories;

import com.capstone.airlineticketreservationsystem.bookings.models.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingRepositoryDAO {

    Booking save(Booking booking);
    Booking update(Booking booking);
    int softDeleteByUUID(String bookingUUID);

    Optional<Booking> findByUUID(String bookingUUID);
    Optional<Long> findIdByUUID(String bookingUUID);
    

    List<Booking> findAll();
    int expirePendingBookings(int minutes);

    Optional<Booking> findById(Long id);
    List<Booking> findAllBookingsByUserId(Long userId);
}
