package com.capstone.airlineticketreservationsystem.flights.services;

import com.capstone.airlineticketreservationsystem.flights.dtos.CreateFlightRequest;
import com.capstone.airlineticketreservationsystem.flights.dtos.FlightDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.FlightSearchCriteria;
import com.capstone.airlineticketreservationsystem.flights.dtos.UpdateFlightRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FlightService {

    private final FlightRepositoryDAO flightRepository;

    public FlightService(FlightRepositoryDAO flightRepository) {
        this.flightRepository = flightRepository;
    }

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

    public Page<FlightDTO> getAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable);
    }

    public FlightDTO getFlightByUUID(String flightUUID) {
        Flight flight = flightRepository.findByFlightUUID(flightUUID)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with UUID: " + flightUUID));
        return buildCompleteFlightDTO(flight);
    }

    public Page<FlightDTO> searchFlights(FlightSearchCriteria criteria, Pageable pageable) {
        // Get paginated results from repository
        Page<FlightDTO> flightPage = flightRepository.searchFlights(criteria, pageable);

        // Apply seat type pricing and custom sorting
        List<FlightDTO> availableFlights = flightPage.getContent().stream()
                .filter(flight -> {
                    Map<String, Integer> availability = flightRepository.getSeatsAvailability(flight.getFlightUUID());
                    if ("business".equalsIgnoreCase(criteria.getSeatType())) {
                        return availability.get("available_business_seats") > 0;
                    } else {
                        return availability.get("available_economy_seats") > 0;
                    }
                })
                .collect(Collectors.toList());

        // Apply pricing based on seat type
        List<FlightDTO> pricedFlights = applySeatTypePricing(availableFlights, criteria.getSeatType());

        // Apply custom sorting: fastest at index 0, cheapest at index 1, then by parameters
        List<FlightDTO> sortedFlights = applyCustomSorting(pricedFlights, criteria);

        return new PageImpl<>(sortedFlights, pageable, flightPage.getTotalElements());
    }

    public FlightDTO updateFlight(String flightUUID, UpdateFlightRequest updateFlightRequest) {

        Flight existingFlight = flightRepository.findByFlightUUID(flightUUID)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with UUID: " + flightUUID));

        EntityLookupService.FlightRequiredIds resolvedIds = entityLookupService.resolveFlightDependencies(updateFlightRequest);

        updateFlightEntity(existingFlight, resolvedIds, updateFlightRequest);

        Flight updatedFlight = flightRepository.update(existingFlight);

        return buildCompleteFlightDTO(updatedFlight);
    }

    public void deleteFlightByUUID(String flightUUID) {
        if (!flightRepository.existsByUUIDAndNotDeleted(flightUUID)) {
            throw new FlightNotFoundException("Flight not found with UUID: " + flightUUID);
        }

        int rowsAffected = flightRepository.softDeleteByUUID(flightUUID);

        if (rowsAffected == 0) {
            throw new FlightAlreadyDeletedException("Flight with UUID: " + flightUUID + " is already deleted");
        }
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

    private List<FlightDTO> applySeatTypePricing(List<FlightDTO> flights, String seatType) {
        return flights.stream()
                .map(flight -> {
                    FlightDTO pricedFlight = cloneFlightDTO(flight); // Implement this based on your needs
                    BigDecimal price = "business".equalsIgnoreCase(seatType)
                            ? flight.getBaseBusinessPrice()
                            : flight.getBaseEconomyPrice();
                    pricedFlight.setCurrentPrice(price); // Add this field to FlightDTO
                    return pricedFlight;
                })
                .collect(Collectors.toList());
    }

    private List<FlightDTO> applyCustomSorting(List<FlightDTO> flights, FlightSearchCriteria criteria) {
        if (flights.isEmpty()) return flights;

        List<FlightDTO> sortedList = new ArrayList<>(flights);

        // Find fastest flight (shortest duration)
        FlightDTO fastest = sortedList.stream()
                .min(Comparator.comparingLong(this::calculateFlightDuration))
                .orElse(sortedList.get(0));

        // Find cheapest flight
        FlightDTO cheapest = sortedList.stream()
                .min(Comparator.comparing(FlightDTO::getCurrentPrice))
                .orElse(sortedList.get(0));

        // Remove fastest and cheapest from the list for remaining sorting
        sortedList.remove(fastest);
        sortedList.remove(cheapest);

        // Sort remaining flights based on request parameters
        sortRemainingFlights(sortedList, criteria);

        // Build final list: fastest at 0, cheapest at 1, then the rest
        List<FlightDTO> finalList = new ArrayList<>();
        finalList.add(fastest);
        finalList.add(cheapest);
        finalList.addAll(sortedList);

        return finalList;
    }

    private void sortRemainingFlights(List<FlightDTO> flights, FlightSearchCriteria criteria) {
        String sortBy = criteria.getSortBy();
        String direction = criteria.getDirection();

        Comparator<FlightDTO> comparator = switch (sortBy) {
            case "duration" -> Comparator.comparingLong(this::calculateFlightDuration);
            case "price" -> Comparator.comparing(FlightDTO::getCurrentPrice);
            default -> Comparator.comparing(FlightDTO::getScheduledDeparture);
        };

        if ("desc".equalsIgnoreCase(direction)) {
            comparator = comparator.reversed();
        }

        flights.sort(comparator);
    }

    private long calculateFlightDuration(FlightDTO flight) {
        return Duration.between(flight.getScheduledDeparture(), flight.getScheduledArrival()).toMinutes();
    }

    private FlightDTO cloneFlightDTO(FlightDTO original) {
        // Simple implementation - create new FlightDTO and copy all fields
        FlightDTO clone = new FlightDTO();

        // Copy all basic fields
        clone.setFlightUUID(original.getFlightUUID());
        clone.setFlightNumber(original.getFlightNumber());
        clone.setScheduledDeparture(original.getScheduledDeparture());
        clone.setScheduledArrival(original.getScheduledArrival());
        clone.setActualDeparture(original.getActualDeparture());
        clone.setActualArrival(original.getActualArrival());
        clone.setStatus(original.getStatus());
        clone.setBaseEconomyPrice(original.getBaseEconomyPrice());
        clone.setBaseBusinessPrice(original.getBaseBusinessPrice());
        clone.setCreatedAt(original.getCreatedAt());
        clone.setUpdatedAt(original.getUpdatedAt());

        // Copy airline information
        clone.setAirlineUUID(original.getAirlineUUID());
        clone.setAirlineCode(original.getAirlineCode());
        clone.setAirlineName(original.getAirlineName());
        clone.setAirlineCountry(original.getAirlineCountry());
        clone.setAirlineLogoUrl(original.getAirlineLogoUrl());

        // Copy aircraft information
        clone.setAircraftTypeUUID(original.getAircraftTypeUUID());
        clone.setAircraftModel(original.getAircraftModel());
        clone.setManufacturer(original.getManufacturer());
        clone.setTotalSeats(original.getTotalSeats());
        clone.setBusinessClassSeats(original.getBusinessClassSeats());
        clone.setEconomyClassSeats(original.getEconomyClassSeats());

        // Copy airport information - these are immutable so we can reuse the objects
        clone.setDepartureAirport(original.getDepartureAirport());
        clone.setArrivalAirport(original.getArrivalAirport());

        // currentPrice will be set separately in applySeatTypePricing
        clone.setCurrentPrice(original.getCurrentPrice());

        return clone;
    }

    private void updateFlightEntity(Flight flight, EntityLookupService.FlightRequiredIds ids, UpdateFlightRequest request) {
        // Update only non-null fields (partial update)
        if (request.getFlightNumber() != null) {
            flight.setFlightNumber(request.getFlightNumber());
        }
        if (request.getAirlineUUID() != null) {
            flight.setAirlineId(ids.airlineId());
        }
        if (request.getAircraftTypeUUID() != null) {
            flight.setAircraftTypeId(ids.aircraftTypeId());
        }
        if (request.getDepartureAirportUUID() != null) {
            flight.setDepartureAirportId(ids.departureAirportId());
        }
        if (request.getArrivalAirportUUID() != null) {
            flight.setArrivalAirportId(ids.arrivalAirportId());
        }
        if (request.getScheduledDeparture() != null) {
            flight.setScheduledDeparture(request.getScheduledDeparture());
        }
        if (request.getScheduledArrival() != null) {
            flight.setScheduledArrival(request.getScheduledArrival());
        }
        if (request.getActualDeparture() != null) {
            flight.setActualDeparture(request.getActualDeparture());
        }
        if (request.getActualArrival() != null) {
            flight.setActualArrival(request.getActualArrival());
        }
        if (request.getStatus() != null) {
            flight.setStatus(request.getStatus());
        }
        if (request.getBaseEconomyPrice() != null) {
            flight.setBaseEconomyPrice(request.getBaseEconomyPrice());
        }
        if (request.getBaseBusinessPrice() != null) {
            flight.setBaseBusinessPrice(request.getBaseBusinessPrice());
        }
    }
}
