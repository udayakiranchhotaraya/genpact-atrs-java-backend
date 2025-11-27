package com.capstone.airlineticketreservationsystem.flights.services;

import com.capstone.airlineticketreservationsystem.flights.dtos.CreateFlightRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.FlightDTO;
import com.capstone.airlineticketreservationsystem.flights.exceptions.*;
import com.capstone.airlineticketreservationsystem.flights.models.AircraftType;
import com.capstone.airlineticketreservationsystem.flights.models.Airline;
import com.capstone.airlineticketreservationsystem.flights.models.Airport;
import com.capstone.airlineticketreservationsystem.flights.models.Flight;
import com.capstone.airlineticketreservationsystem.flights.repositories.AircraftTypeRepositoryDAO;
import com.capstone.airlineticketreservationsystem.flights.repositories.AirlineRepositoryDAO;
import com.capstone.airlineticketreservationsystem.flights.repositories.AirportRepositoryDAO;
import com.capstone.airlineticketreservationsystem.flights.repositories.FlightRepositoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlightService {

    @Autowired
    private FlightRepositoryDAO flightRepository;

    @Autowired
    private EntityLookupService entityLookupService;

    @Autowired
    private AirlineRepositoryDAO airlineRepository;

    @Autowired
    private AirportRepositoryDAO airportRepository;

    @Autowired
    private AircraftTypeRepositoryDAO aircraftTypeRepository;

    public FlightDTO createFlight(CreateFlightRequest createFlightRequest) {
        // Step 1: Convert UUIDs to internal IDs using EntityLookupService
        EntityLookupService.FlightRequiredIds ids = entityLookupService.resolveFlightDependencies(createFlightRequest);

        // Step 2: Validate business rules
        validateFlightBusinessRules(ids, createFlightRequest);

        // Step 3: Create and save flight entity
        Flight flight = createFlightEntity(ids, createFlightRequest);
        Flight savedFlight = flightRepository.save(flight);

        // Step 4: Build complete DTO with all related entity details
        return buildCompleteFlightDTO(savedFlight);
    }

    private void validateFlightBusinessRules(EntityLookupService.FlightRequiredIds ids, CreateFlightRequest request) {
        // Validate departure and arrival airports are different
        if (ids.departureAirportId().equals(ids.arrivalAirportId())) {
            throw new InvalidFlightRouteException("Departure and arrival airports cannot be the same");
        }

        // Validate scheduled times
        if (request.getScheduledArrival().isBefore(request.getScheduledDeparture())) {
            throw new InvalidFlightScheduleException("Arrival time must be after departure time");
        }

        // Validate prices
        if (request.getBaseBusinessPrice().compareTo(request.getBaseEconomyPrice()) <= 0) {
            throw new InvalidFlightPricingException("Business class price must be higher than economy class price");
        }
    }

    private Flight createFlightEntity(EntityLookupService.FlightRequiredIds ids, CreateFlightRequest request) {
        Flight flight = new Flight();
        flight.setAirlineId(ids.airlineId());
        flight.setAircraftTypeId(ids.aircraftTypeId());
        flight.setFlightNumber(request.getFlightNumber());
        flight.setDepartureAirportId(ids.departureAirportId());
        flight.setArrivalAirportId(ids.arrivalAirportId());
        flight.setScheduledDeparture(request.getScheduledDeparture());
        flight.setScheduledArrival(request.getScheduledArrival());
        flight.setBaseEconomyPrice(request.getBaseEconomyPrice());
        flight.setBaseBusinessPrice(request.getBaseBusinessPrice());
        flight.setStatus(request.getStatus());
        return flight;
    }

    /**
     * Builds a complete FlightDTO with all related entity details
     */
    private FlightDTO buildCompleteFlightDTO(Flight flight) {
        FlightDTO flightDTO = new FlightDTO();

        // Set basic flight information
        setBasicFlightInfo(flightDTO, flight);

        // Fetch and set airline details
        setAirlineDetails(flightDTO, flight.getAirlineId());

        // Fetch and set aircraft type details
        setAircraftTypeDetails(flightDTO, flight.getAircraftTypeId());

        // Fetch and set airport details
        setAirportDetails(flightDTO, flight.getDepartureAirportId(), flight.getArrivalAirportId());

        return flightDTO;
    }

    private void setBasicFlightInfo(FlightDTO flightDTO, Flight flight) {
        flightDTO.setFlightUUID(flight.getFlightUUID());
        flightDTO.setFlightNumber(flight.getFlightNumber());
        flightDTO.setScheduledDeparture(flight.getScheduledDeparture());
        flightDTO.setScheduledArrival(flight.getScheduledArrival());
        flightDTO.setActualDeparture(flight.getActualDeparture());
        flightDTO.setActualArrival(flight.getActualArrival());
        flightDTO.setStatus(flight.getStatus());
        flightDTO.setBaseEconomyPrice(flight.getBaseEconomyPrice());
        flightDTO.setBaseBusinessPrice(flight.getBaseBusinessPrice());
        flightDTO.setCreatedAt(flight.getCreatedAt());
    }

    private void setAirlineDetails(FlightDTO flightDTO, Long airlineId) {
        Airline airline = airlineRepository.findById(airlineId)
                .orElseThrow(() -> new AirlineNotFoundException("Airline not found with ID: " + airlineId));

        flightDTO.setAirlineUUID(airline.getAirlineUUID());
        flightDTO.setAirlineCode(airline.getAirlineCode());
        flightDTO.setAirlineName(airline.getAirlineName());
        flightDTO.setAirlineCountry(airline.getCountry());
        flightDTO.setAirlineLogoUrl(airline.getLogoURL());
    }

    private void setAircraftTypeDetails(FlightDTO flightDTO, Long aircraftTypeId) {
        AircraftType aircraftType = aircraftTypeRepository.findById(aircraftTypeId)
                .orElseThrow(() -> new AircraftTypeNotFoundException("Aircraft type not found with ID: " + aircraftTypeId));

        flightDTO.setAircraftTypeUUID(aircraftType.getAircraftTypeUUID());
        flightDTO.setAircraftModel(aircraftType.getAircraftModel());
        flightDTO.setManufacturer(aircraftType.getManufacturer());
        flightDTO.setTotalSeats(aircraftType.getTotalSeats());
        flightDTO.setBusinessClassSeats(aircraftType.getBusinessClassSeats());
        flightDTO.setEconomyClassSeats(aircraftType.getEconomyClassSeats());
    }

    private void setAirportDetails(FlightDTO flightDTO, Long departureAirportId, Long arrivalAirportId) {
        // Set departure airport details
        Airport departureAirport = airportRepository.findById(departureAirportId)
                .orElseThrow(() -> new AirportNotFoundException("Departure airport not found with ID: " + departureAirportId));

        FlightDTO.AirportInfo departureInfo = createAirportInfo(departureAirport);
        flightDTO.setDepartureAirport(departureInfo);

        // Set arrival airport details
        Airport arrivalAirport = airportRepository.findById(arrivalAirportId)
                .orElseThrow(() -> new AirportNotFoundException("Arrival airport not found with ID: " + arrivalAirportId));

        FlightDTO.AirportInfo arrivalInfo = createAirportInfo(arrivalAirport);
        flightDTO.setArrivalAirport(arrivalInfo);
    }

    private FlightDTO.AirportInfo createAirportInfo(Airport airport) {
        FlightDTO.AirportInfo airportInfo = new FlightDTO.AirportInfo();
        airportInfo.setAirportUUID(airport.getAirportUUID());
        airportInfo.setAirportCode(airport.getAirportCode());
        airportInfo.setAirportName(airport.getAirportName());
        airportInfo.setCity(airport.getCity());
        airportInfo.setCountry(airport.getCountry());
        airportInfo.setTimezone(airport.getTimezone());
        return airportInfo;
    }
}
