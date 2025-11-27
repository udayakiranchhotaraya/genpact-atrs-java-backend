package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

import com.capstone.airlineticketreservationsystem.flights.models.FlightStatus;
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

    private static class FlightRowMapper implements RowMapper<Flight> {
        @Override
        public Flight mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
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
}
