package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.capstone.airlineticketreservationsystem.flights.models.Airport;

import static com.capstone.airlineticketreservationsystem.utilities.UUIDV7Generator.generateUUIDV7;

@Repository
public class AirportRepositoryImplDAO implements AirportRepositoryDAO {

    public AirportRepositoryImplDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Airport save(Airport airport) {

        String sql = "INSERT INTO airports (airports_uuid, airport_code, airport_name, city, country, timezone) VALUES (?, ?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        airport.setAirportUUID(generateUUIDV7().toString());

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, airport.getAirportUUID());
            preparedStatement.setString(2, airport.getAirportCode());
            preparedStatement.setString(3, airport.getAirportName());
            preparedStatement.setString(4, airport.getCity());
            preparedStatement.setString(5, airport.getCountry());
            preparedStatement.setString(6, airport.getTimezone());
            return preparedStatement;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            airport.setId(key.longValue());
            // Fetch and set the timestamps
            String selectSql = "SELECT created_at FROM airports WHERE id = ?";
            return jdbcTemplate.queryForObject(selectSql, (rs, rowNum) -> {
                airport.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return airport;
            }, key.longValue());
        }
        throw new RuntimeException("Failed to insert airport and retrieve generated ID.");
    }
}
