package com.capstone.airlineticketreservationsystem.bookings.scheduler;

import com.capstone.airlineticketreservationsystem.bookings.repositories.BookingRepositoryDAO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BookingCleanupScheduler {

    private final BookingRepositoryDAO bookingRepository;

    public BookingCleanupScheduler(BookingRepositoryDAO bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    // Runs every 10 minutes
    @Scheduled(fixedRate = 600000)
    public void expireOldPendingBookings() {
        int count = bookingRepository.expirePendingBookings(15); // expire after 15 minutes
        System.out.println("Expired pending bookings: " + count);
    }
}
