package com.capstone.airlineticketreservationsystem.flights.services;

import com.capstone.airlineticketreservationsystem.flights.dtos.CreateFlightRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.UpdateFlightRequest;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AircraftTypeNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AirlineNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AirportNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.repositories.AircraftTypeRepositoryDAO;
import com.capstone.airlineticketreservationsystem.flights.repositories.AirlineRepositoryDAO;
import com.capstone.airlineticketreservationsystem.flights.repositories.AirportRepositoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntityLookupService {

    @Autowired
    private AirlineRepositoryDAO airlineRepository;

    @Autowired
    private AirportRepositoryDAO airportRepository;

    @Autowired
    private AircraftTypeRepositoryDAO aircraftTypeRepository;

    public Long getAirlineIdByUUID(String airlineUUID) {
        return airlineRepository.findIdByUUID(airlineUUID)
                .orElseThrow(() -> new AirlineNotFoundException("Airline not found with UUID: " + airlineUUID));
    }

    public Long getAirportIdByUUID(String airportUUID) {
        return airportRepository.findIdByUUID(airportUUID)
                .orElseThrow(() -> new AirportNotFoundException("Airport not found with UUID: " + airportUUID));
    }

    public Long getAircraftTypeIdByUUID(String aircraftTypeUUID) {
        return aircraftTypeRepository.findIdByUUID(aircraftTypeUUID)
                .orElseThrow(() -> new AircraftTypeNotFoundException("Aircraft type not found with UUID: " + aircraftTypeUUID));
    }

    public FlightRequiredIds resolveFlightDependencies(CreateFlightRequest createFlightRequest) {
        Long airlineId = getAirlineIdByUUID(createFlightRequest.getAirlineUUID());
        Long aircraftTypeId = getAircraftTypeIdByUUID(createFlightRequest.getAircraftTypeUUID());
        Long departureAirportId = getAirportIdByUUID(createFlightRequest.getDepartureAirportUUID());
        Long arrivalAirportId = getAirportIdByUUID(createFlightRequest.getArrivalAirportUUID());

        return new FlightRequiredIds(airlineId, aircraftTypeId, departureAirportId, arrivalAirportId);
    }

    public FlightRequiredIds resolveFlightDependencies(UpdateFlightRequest updateFlightRequest) {
        Long airlineId = getAirlineIdByUUID(updateFlightRequest.getAirlineUUID());
        Long aircraftTypeId = getAircraftTypeIdByUUID(updateFlightRequest.getAircraftTypeUUID());
        Long departureAirportId = getAirportIdByUUID(updateFlightRequest.getDepartureAirportUUID());
        Long arrivalAirportId = getAirportIdByUUID(updateFlightRequest.getArrivalAirportUUID());

        return new FlightRequiredIds(airlineId, aircraftTypeId, departureAirportId, arrivalAirportId);
    }

    // Record for clean data transfer between service layers
    public static record FlightRequiredIds(
            Long airlineId,
            Long aircraftTypeId,
            Long departureAirportId,
            Long arrivalAirportId
    ) {}
}
