package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.capstone.airlineticketreservationsystem.flights.models.Airline;
import static com.capstone.airlineticketreservationsystem.utilities.UUIDV7Generator.generateUUIDV7;

@Repository
public class AirlineRepositoryImplDAO implements AirlineRepositoryDAO {

    public AirlineRepositoryImplDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Airline save(Airline airline) {

        String sql = "INSERT INTO airlines (airlines_uuid, airline_code, airline_name, country, logo_url) VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        airline.setAirlineUUID(generateUUIDV7().toString());

        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1, airline.getAirlineUUID());
            preparedStatement.setString(2, airline.getAirlineCode());
            preparedStatement.setString(3, airline.getAirlineName());
            preparedStatement.setString(4, airline.getCountry());
            preparedStatement.setString(5, airline.getLogoURL());

            return  preparedStatement;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            airline.setId(key.longValue());

            String selectSql = "SELECT created_at FROM airlines WHERE id = ?";
            return jdbcTemplate.queryForObject(selectSql, (rs, rowNum) -> {
                airline.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return airline;
            }, key.longValue());
        }
        return null;
    }
}
