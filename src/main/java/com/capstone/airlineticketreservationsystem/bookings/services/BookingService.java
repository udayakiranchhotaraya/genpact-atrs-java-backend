package com.capstone.airlineticketreservationsystem.bookings.services;

import com.capstone.airlineticketreservationsystem.bookings.dtos.BookingDTO;
import com.capstone.airlineticketreservationsystem.bookings.dtos.CreateBookingRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.UpdateBookingStatusRequest;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.BookingNotFoundException;
import com.capstone.airlineticketreservationsystem.bookings.models.Booking;
import com.capstone.airlineticketreservationsystem.bookings.models.BookingStatus;
import com.capstone.airlineticketreservationsystem.bookings.repositories.BookingRepositoryDAO;
import com.capstone.airlineticketreservationsystem.flights.exceptions.FlightNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.repositories.FlightRepositoryDAO;
import com.capstone.airlineticketreservationsystem.utilities.PNRGenerator;
import com.capstone.airlineticketreservationsystem.users.exceptions.UserNotFoundException;
import com.capstone.airlineticketreservationsystem.users.repositories.UserRepositoryDAO;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepositoryDAO bookingRepository;
    private final UserRepositoryDAO userRepository;
    private final FlightRepositoryDAO flightRepository;

    public BookingService(
            BookingRepositoryDAO bookingRepository,
            UserRepositoryDAO userRepository,
            FlightRepositoryDAO flightRepository
    ) {
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
    }

    // ================= CREATE BOOKING ====================
    public BookingDTO createBooking(CreateBookingRequest request) {

        // 1. Validate user
        Long userId = userRepository.findIdByUUID(request.getUserUUID())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with UUID: " + request.getUserUUID()));

        // 2. Validate flight
        Long flightId = flightRepository.findIdByUUID(request.getFlightUUID())
                .orElseThrow(() ->
                        new FlightNotFoundException("Flight not found with UUID: " + request.getFlightUUID()));

        // 3. BLOCK SEATS ✔ (count = passengers size)
        int updatedRows = flightRepository.reduceSeatCount(
                flightId,
                request.getSeatClass().name(),
                request.getPassengers().size()
        );

        if (updatedRows == 0) {
            throw new RuntimeException("Not enough seats available in " + request.getSeatClass().name());
        }

        // 4. Create booking
        Booking booking = new Booking();
        booking.setUserId(userId);
        booking.setFlightId(flightId);
        booking.setSeatClass(request.getSeatClass());
        booking.setPnr(PNRGenerator.generatePNR());
        booking.setBookingStatus(BookingStatus.PENDING);
        booking.setDeleted(false);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        Booking saved = bookingRepository.save(booking);
        return convertToDTO(saved);
    }

    public BookingDTO initiateBooking(CreateBookingRequest request) {
        return createBooking(request);
    }

    // ================= GET BOOKING ====================
    public BookingDTO getBooking(String bookingUUID) {
        Booking booking = bookingRepository.findByUUID(bookingUUID)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));
        return convertToDTO(booking);
    }

    // ================= LIST ALL ====================
    public List<BookingDTO> getAllBookings() {
        return bookingRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ================= UPDATE STATUS ====================
    public BookingDTO updateStatus(String bookingUUID, UpdateBookingStatusRequest req) {

        Booking booking = bookingRepository.findByUUID(bookingUUID)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found"));

        booking.setBookingStatus(req.getBookingStatus());
        Booking updated = bookingRepository.update(booking);

        return convertToDTO(updated);
    }

    // ================= DELETE ====================
    public void deleteBooking(String bookingUUID) {
        if (bookingRepository.softDeleteByUUID(bookingUUID) == 0) {
            throw new BookingNotFoundException("Booking already deleted or not found");
        }
    }

    public List<BookingDTO> getAllBookingsByUserUUID(String userUUID) {
        // Get user's internal ID from UUID
        Long userId = userRepository.findIdByUUID(userUUID)
                .orElseThrow(() -> new RuntimeException("User not found with UUID: " + userUUID));

        List<Booking> bookings = bookingRepository.findAllBookingsByUserId(userId);

        // Convert to DTOs
        return bookings.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ================= CONVERTER ====================
    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();

        dto.setBookingUUID(booking.getBookingsUUID());
        dto.setPnr(booking.getPnr());
        dto.setUserUUID(
                userRepository.findById(booking.getUserId()).orElseThrow().getUserUUID()
        );
        dto.setBookingStatus(booking.getBookingStatus());
        dto.setCreatedAt(booking.getCreatedAt());
        dto.setUpdatedAt(booking.getUpdatedAt());

        if (booking.getSeatClass() != null) {
            dto.setSeatClass(booking.getSeatClass().name());
        }

        return dto;
    }
}
