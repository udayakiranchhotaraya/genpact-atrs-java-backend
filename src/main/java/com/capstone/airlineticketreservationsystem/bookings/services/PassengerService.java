package com.capstone.airlineticketreservationsystem.bookings.services;

import com.capstone.airlineticketreservationsystem.bookings.dtos.CreatePassengerRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.PassengerDTO;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.PassengerCreationException;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.PassengerNotFoundException;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.PassengerValidationException;
import com.capstone.airlineticketreservationsystem.bookings.models.Passenger;
import com.capstone.airlineticketreservationsystem.bookings.models.PassengerType;
import com.capstone.airlineticketreservationsystem.bookings.repositories.PassengerRepositoryDAO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PassengerService {

    private final PassengerRepositoryDAO passengerRepository;

    public PassengerService(PassengerRepositoryDAO passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    // CREATE PASSENGER
    public PassengerDTO createPassenger(CreatePassengerRequest request) {

    	if (request.getPassportNumber() == null || request.getPassportNumber().isEmpty()) {
            throw new PassengerValidationException("Passport number is required");
        }
    	if (request.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new PassengerValidationException("Date of birth cannot be in the future");
        }
        Passenger passenger = new Passenger();
        passenger.setBookingId(request.getBookingId());
        passenger.setFirstName(request.getFirstName());
        passenger.setLastName(request.getLastName());
        passenger.setEmail(request.getEmail());
        passenger.setDateOfBirth(request.getDateOfBirth());
        passenger.setPassportNumber(request.getPassportNumber());
        passenger.setNationality(request.getNationality());
        passenger.setPassengerType(PassengerType.valueOf(request.getPassengerType().toUpperCase()));

        Passenger saved = passengerRepository.save(passenger);
        // Check save failure
        if (saved == null) {
            throw new PassengerCreationException("Failed to create passenger");
        }
        return convertToDTO(saved);
    }

    // GET BY UUID
    public PassengerDTO getPassenger(String passengersUUID) {
        Passenger passenger = passengerRepository.findByUUID(passengersUUID)
                .orElseThrow(() -> new PassengerNotFoundException("Passenger not found"));

        return convertToDTO(passenger);
    }

    // GET ALL PASSENGERS IN A BOOKING
    public List<PassengerDTO> getPassengersByBooking(Long bookingId) {
        return passengerRepository.findByBookingId(bookingId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // UPDATE PASSENGER
    public PassengerDTO updatePassenger(String passengersUUID, CreatePassengerRequest request) {

        Passenger passenger = passengerRepository.findByUUID(passengersUUID)
                .orElseThrow(() -> new PassengerNotFoundException("Passenger not found"));

        passenger.setFirstName(request.getFirstName());
        passenger.setLastName(request.getLastName());
        passenger.setEmail(request.getEmail());
        passenger.setDateOfBirth(request.getDateOfBirth());
        passenger.setPassportNumber(request.getPassportNumber());
        passenger.setNationality(request.getNationality());
        passenger.setPassengerType(PassengerType.valueOf(request.getPassengerType().toUpperCase()));

        Passenger updated = passengerRepository.update(passenger);
        return convertToDTO(updated);
    }

    // DELETE (SOFT DELETE)
    public void deletePassenger(String passengersUUID) {
        int result = passengerRepository.softDelete(passengersUUID);

        if (result == 0) {
            throw new PassengerNotFoundException("Passenger not found or already deleted");
        }
    }

    // CONVERT TO DTO
    private PassengerDTO convertToDTO(Passenger p) {
        PassengerDTO dto = new PassengerDTO();

        dto.setPassengersUUID(p.getPassengersUUID());
        dto.setBookingId(p.getBookingId());
        dto.setUserId(p.getUserId());
        dto.setFirstName(p.getFirstName());
        dto.setLastName(p.getLastName());
        dto.setEmail(p.getEmail());
        dto.setDateOfBirth(p.getDateOfBirth());
        dto.setPassportNumber(p.getPassportNumber());
        dto.setNationality(p.getNationality());
        dto.setPassengerType(p.getPassengerType());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());

        return dto;
    }
}
