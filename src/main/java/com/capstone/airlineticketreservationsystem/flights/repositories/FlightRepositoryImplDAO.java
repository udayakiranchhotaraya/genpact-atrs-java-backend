package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.capstone.airlineticketreservationsystem.flights.dtos.FlightDTO;
import com.capstone.airlineticketreservationsystem.flights.dtos.FlightSearchCriteria;
import com.capstone.airlineticketreservationsystem.flights.models.FlightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.capstone.airlineticketreservationsystem.flights.models.Flight;

import static com.capstone.airlineticketreservationsystem.utilities.UUIDV7Generator.generateUUIDV7;

@Repository
public class FlightRepositoryImplDAO implements FlightRepositoryDAO {

    public FlightRepositoryImplDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public final JdbcTemplate jdbcTemplate;

    @Override
    public Flight save(Flight flight) {

        String sql = "INSERT INTO flights (flights_uuid, airline_id, aircraft_type_id, flight_number, departure_airport_id, arrival_airport_id, scheduled_departure, scheduled_arrival, base_economy_price, base_business_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        flight.setFlightUUID(generateUUIDV7().toString());

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, flight.getFlightUUID());
            preparedStatement.setLong(2, flight.getAirlineId());
            preparedStatement.setLong(3, flight.getAircraftTypeId());
            preparedStatement.setString(4, flight.getFlightNumber());
            preparedStatement.setLong(5, flight.getDepartureAirportId());
            preparedStatement.setLong(6, flight.getArrivalAirportId());
            preparedStatement.setTimestamp(7, java.sql.Timestamp.valueOf(flight.getScheduledDeparture()));
            preparedStatement.setTimestamp(8, java.sql.Timestamp.valueOf(flight.getScheduledArrival()));
            preparedStatement.setBigDecimal(9, flight.getBaseEconomyPrice());
            preparedStatement.setBigDecimal(10, flight.getBaseBusinessPrice());
            return preparedStatement;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            flight.setId(key.longValue());

            String selectSql = "SELECT created_at FROM flights WHERE id = ?";
            return jdbcTemplate.queryForObject(selectSql, (rs, rowNum) -> {
                flight.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return flight;
            }, key.longValue());
        }
        throw new RuntimeException("Failed to insert flight and retrieve generated ID.");
    }

    @Override
    public Optional<Flight> findByFlightUUID(String flightUUID) {
        String sql = "SELECT * FROM flights WHERE flights_uuid = ? AND is_deleted = false";
        try {
            Flight flight = jdbcTemplate.queryForObject(sql, new FlightRowMapper(), flightUUID);
            return Optional.ofNullable(flight);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public Optional<Flight> findById(Long id) {
        String sql = "SELECT * FROM flights WHERE id = ? AND is_deleted = false";
        try {
            Flight flight = jdbcTemplate.queryForObject(sql, new FlightRowMapper(), id);
            return Optional.ofNullable(flight);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<FlightDTO> findAll(Pageable pageable) {
        // Main data query with JOINs
        String dataSql = """
            SELECT 
                f.flight_uuid, f.flight_number,
                f.scheduled_departure, f.scheduled_arrival,
                f.actual_departure, f.actual_arrival,
                f.status, f.base_economy_price, f.base_business_price,
                f.created_at, f.updated_at,
                
                -- Airline details
                a.airlines_uuid AS airline_uuid, a.airline_code, 
                a.airline_name, a.country AS airline_country, a.logo_url AS airline_logo_url,
                
                -- Aircraft type details  
                ac.aircraft_types_uuid AS aircraft_type_uuid, ac.aircraft_model,
                ac.manufacturer, ac.total_seats, ac.business_class_seats, ac.economy_class_seats,
                
                -- Departure airport details
                dep.airports_uuid AS dep_airport_uuid, dep.airport_code AS dep_airport_code,
                dep.airport_name AS dep_airport_name, dep.city AS dep_city, 
                dep.country AS dep_country, dep.timezone AS dep_timezone,
                
                -- Arrival airport details
                arr.airports_uuid AS arr_airport_uuid, arr.airport_code AS arr_airport_code,
                arr.airport_name AS arr_airport_name, arr.city AS arr_city,
                arr.country AS arr_country, arr.timezone AS arr_timezone
                
            FROM flights f
            INNER JOIN airlines a ON f.airline_id = a.id AND a.is_deleted = FALSE
            INNER JOIN aircraft_types ac ON f.aircraft_type_id = ac.id AND ac.is_deleted = FALSE
            INNER JOIN airports dep ON f.departure_airport_id = dep.id AND dep.is_deleted = FALSE
            INNER JOIN airports arr ON f.arrival_airport_id = arr.id AND arr.is_deleted = FALSE
            WHERE f.is_deleted = FALSE
            ORDER BY f.scheduled_departure DESC
            LIMIT ? OFFSET ?
            """;

        // Execute paginated query
        List<FlightDTO> content = jdbcTemplate.query(
                dataSql,
                new FlightDtoRowMapper(),
                pageable.getPageSize(),
                pageable.getOffset()
        );

        // Count query for pagination metadata
        String countSql = "SELECT COUNT(*) FROM flights WHERE is_deleted = FALSE";
        Long total = jdbcTemplate.queryForObject(countSql, Long.class);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<FlightDTO> searchFlights(FlightSearchCriteria criteria, Pageable pageable) {
        // Build WHERE clause and parameters
        StringBuilder whereClause = new StringBuilder("WHERE f.is_deleted = FALSE ");
        List<Object> params = new ArrayList<>();

        // Airport filters
        if (criteria.getDepartureAirportUUID() != null) {
            whereClause.append("AND dep.airports_uuid = ? ");
            params.add(criteria.getDepartureAirportUUID());
        }

        if (criteria.getArrivalAirportUUID() != null) {
            whereClause.append("AND arr.airports_uuid = ? ");
            params.add(criteria.getArrivalAirportUUID());
        }

        if (criteria.getAirlineUUID() != null) {
            whereClause.append("AND a.airlines_uuid = ? ");
            params.add(criteria.getAirlineUUID());
        }

        // Seat availability filter - assuming you have available_seats fields
        if ("business".equalsIgnoreCase(criteria.getSeatType())) {
            whereClause.append("AND f.available_business_seats > 0 ");
        } else {
            whereClause.append("AND f.available_economy_seats > 0 ");
        }

        String dataSql = """
            SELECT
                f.flight_uuid, f.flight_number,
                f.scheduled_departure, f.scheduled_arrival,
                f.actual_departure, f.actual_arrival,
                f.status, f.base_economy_price, f.base_business_price,
                f.created_at, f.updated_at,
                a.airlines_uuid AS airline_uuid, a.airline_code,
                a.airline_name, a.country AS airline_country, a.logo_url AS airline_logo_url,
                ac.aircraft_types_uuid AS aircraft_type_uuid, ac.aircraft_model,
                ac.manufacturer, ac.total_seats, ac.business_class_seats, ac.economy_class_seats,
                dep.airports_uuid AS dep_airport_uuid, dep.airport_code AS dep_airport_code,
                dep.airport_name AS dep_airport_name, dep.city AS dep_city,
                dep.country AS dep_country, dep.timezone AS dep_timezone,
                arr.airports_uuid AS arr_airport_uuid, arr.airport_code AS arr_airport_code,
                arr.airport_name AS arr_airport_name, arr.city AS arr_city,
                arr.country AS arr_country, arr.timezone AS arr_timezone,
                CASE WHEN ? = 'business' THEN f.base_business_price ELSE f.base_economy_price END AS current_price
            FROM flights f
            INNER JOIN airlines a ON f.airline_id = a.id AND a.is_deleted = FALSE
            INNER JOIN aircraft_types ac ON f.aircraft_type_id = ac.id AND ac.is_deleted = FALSE
            INNER JOIN airports dep ON f.departure_airport_id = dep.id AND dep.is_deleted = FALSE
            INNER JOIN airports arr ON f.arrival_airport_id = arr.id AND arr.is_deleted = FALSE
            %s
            ORDER BY f.scheduled_departure ASC
            LIMIT ? OFFSET ?
        """.formatted(whereClause.toString());

        // Add seat type parameter for the CASE statement
        params.add(0, criteria.getSeatType() != null ? criteria.getSeatType() : "economy");

        // Add pagination parameters
        params.add(pageable.getPageSize());
        params.add(pageable.getOffset());

        // Execute query
        List<FlightDTO> content = jdbcTemplate.query(
                dataSql,
                new FlightDtoRowMapper(),
                params.toArray()
        );

        // Count query (excluding price selection and pagination)
        String countSql = "SELECT COUNT(*) FROM flights f " +
                "INNER JOIN airlines a ON f.airline_id = a.id AND a.is_deleted = FALSE " +
                "INNER JOIN airports dep ON f.departure_airport_id = dep.id AND dep.is_deleted = FALSE " +
                "INNER JOIN airports arr ON f.arrival_airport_id = arr.id AND arr.is_deleted = FALSE " +
                whereClause.toString();

        Long total = jdbcTemplate.queryForObject(
                countSql,
                Long.class,
                params.subList(1, params.size() - 2).toArray() // Exclude seatType, LIMIT, OFFSET
        );

        return new PageImpl<>(content, pageable, total);
    }

    private static class FlightRowMapper implements RowMapper<Flight> {
        @Override
        public Flight mapRow(ResultSet rs, int rowNum) throws SQLException {
            Flight flight = new Flight();

            // Basic identifiers
            flight.setId(rs.getLong("id"));
            flight.setFlightUUID(rs.getString("flights_uuid"));

            // Foreign key references
            flight.setAirlineId(rs.getLong("airline_id"));
            flight.setAircraftTypeId(rs.getLong("aircraft_type_id"));
            flight.setFlightNumber(rs.getString("flight_number"));
            flight.setDepartureAirportId(rs.getLong("departure_airport_id"));
            flight.setArrivalAirportId(rs.getLong("arrival_airport_id"));

            // Scheduled times
            flight.setScheduledDeparture(rs.getTimestamp("scheduled_departure").toLocalDateTime());
            flight.setScheduledArrival(rs.getTimestamp("scheduled_arrival").toLocalDateTime());

            // Actual times (nullable)
            flight.setActualDeparture(
                    rs.getTimestamp("actual_departure") != null
                            ? rs.getTimestamp("actual_departure").toLocalDateTime()
                            : null
            );

            flight.setActualArrival(
                    rs.getTimestamp("actual_arrival") != null
                            ? rs.getTimestamp("actual_arrival").toLocalDateTime()
                            : null
            );

            // Flight status
            String statusStr = rs.getString("status");
            if (statusStr != null) {
                try {
                    flight.setStatus(FlightStatus.valueOf(statusStr));
                } catch (IllegalArgumentException e) {
                    flight.setStatus(FlightStatus.SCHEDULED); // Default fallback
                }
            } else {
                flight.setStatus(FlightStatus.SCHEDULED);
            }

            // Pricing
            flight.setBaseEconomyPrice(rs.getBigDecimal("base_economy_price"));
            flight.setBaseBusinessPrice(rs.getBigDecimal("base_business_price"));

            // Metadata
            flight.setDeleted(rs.getBoolean("is_deleted"));
            flight.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            flight.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

            return flight;
        }
    }

    private static class FlightDtoRowMapper implements RowMapper<FlightDTO> {

        @Override
        public FlightDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            FlightDTO flightDTO = new FlightDTO();

            // Basic flight information
            flightDTO.setFlightUUID(rs.getString("flight_uuid"));
            flightDTO.setFlightNumber(rs.getString("flight_number"));
            flightDTO.setScheduledDeparture(rs.getTimestamp("scheduled_departure").toLocalDateTime());
            flightDTO.setScheduledArrival(rs.getTimestamp("scheduled_arrival").toLocalDateTime());

            // Handle potentially null actual timestamps
            Timestamp actualDeparture = rs.getTimestamp("actual_departure");
            flightDTO.setActualDeparture(actualDeparture != null ? actualDeparture.toLocalDateTime() : null);

            Timestamp actualArrival = rs.getTimestamp("actual_arrival");
            flightDTO.setActualArrival(actualArrival != null ? actualArrival.toLocalDateTime() : null);

            flightDTO.setStatus(FlightStatus.valueOf(rs.getString("status")));
            flightDTO.setBaseEconomyPrice(rs.getBigDecimal("base_economy_price"));
            flightDTO.setBaseBusinessPrice(rs.getBigDecimal("base_business_price"));
            flightDTO.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            flightDTO.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

            // Airline information
            flightDTO.setAirlineUUID(rs.getString("airline_uuid"));
            flightDTO.setAirlineCode(rs.getString("airline_code"));
            flightDTO.setAirlineName(rs.getString("airline_name"));
            flightDTO.setAirlineCountry(rs.getString("airline_country"));
            flightDTO.setAirlineLogoUrl(rs.getString("airline_logo_url"));

            // Aircraft type information
            flightDTO.setAircraftTypeUUID(rs.getString("aircraft_type_uuid"));
            flightDTO.setAircraftModel(rs.getString("aircraft_model"));
            flightDTO.setManufacturer(rs.getString("manufacturer"));
            flightDTO.setTotalSeats(rs.getInt("total_seats"));
            flightDTO.setBusinessClassSeats(rs.getInt("business_class_seats"));
            flightDTO.setEconomyClassSeats(rs.getInt("economy_class_seats"));

            // Departure airport information
            FlightDTO.AirportInfo departureAirport = new FlightDTO.AirportInfo();
            departureAirport.setAirportUUID(rs.getString("dep_airport_uuid"));
            departureAirport.setAirportCode(rs.getString("dep_airport_code"));
            departureAirport.setAirportName(rs.getString("dep_airport_name"));
            departureAirport.setCity(rs.getString("dep_city"));
            departureAirport.setCountry(rs.getString("dep_country"));
            departureAirport.setTimezone(rs.getString("dep_timezone"));
            flightDTO.setDepartureAirport(departureAirport);

            // Arrival airport information
            FlightDTO.AirportInfo arrivalAirport = new FlightDTO.AirportInfo();
            arrivalAirport.setAirportUUID(rs.getString("arr_airport_uuid"));
            arrivalAirport.setAirportCode(rs.getString("arr_airport_code"));
            arrivalAirport.setAirportName(rs.getString("arr_airport_name"));
            arrivalAirport.setCity(rs.getString("arr_city"));
            arrivalAirport.setCountry(rs.getString("arr_country"));
            arrivalAirport.setTimezone(rs.getString("arr_timezone"));
            flightDTO.setArrivalAirport(arrivalAirport);

            return flightDTO;
        }
    }
}
