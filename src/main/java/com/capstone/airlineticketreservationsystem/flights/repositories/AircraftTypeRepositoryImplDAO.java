package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.capstone.airlineticketreservationsystem.flights.models.AircraftType;

import static com.capstone.airlineticketreservationsystem.utilities.UUIDV7Generator.generateUUIDV7;

public class AircraftTypeRepositoryImplDAO implements AircraftTypeRepositoryDAO {

    public AircraftTypeRepositoryImplDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;

    @Override
    public AircraftType save(AircraftType aircraftType) {

        String sql = "INSERT INTO aircraft_types (aircraft_types_uuid, aircraft_model, manufacturer, total_seats, business_class_seats, economy_class_seats) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        aircraftType.setAircraftTypeUUID(generateUUIDV7().toString());

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, aircraftType.getAircraftTypeUUID());
            ps.setString(2, aircraftType.getAircraftModel());
            ps.setString(3, aircraftType.getManufacturer());
            ps.setInt(4, aircraftType.getTotalSeats());
            ps.setInt(5, aircraftType.getBusinessClassSeats());
            ps.setInt(6, aircraftType.getEconomyClassSeats());
            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            aircraftType.setId(key.longValue());

            String selectSql = "SELECT created_at FROM aircraft_types WHERE id = ?";
            return jdbcTemplate.queryForObject(selectSql, (rs, rowNum) -> {
                aircraftType.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return aircraftType;
            }, key.longValue());
        }
        throw new RuntimeException("Failed to insert aircraft type and retrieve generated ID.");
    }
}
