package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import com.capstone.airlineticketreservationsystem.flights.exceptions.AirportNotFoundException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    @Override
    public List<Airport> findAll() {
        String sql = "SELECT * FROM airports WHERE is_deleted = false ORDER BY airport_name";
        return jdbcTemplate.query(sql, new AirportRowMapper());
    }

    public Optional<Airport> findByAirportUUID(String airportUUID) {
        String sql = "SELECT * FROM airports WHERE airports_uuid = ? AND is_deleted = false";
        try {
            Airport airport = jdbcTemplate.queryForObject(sql, new AirportRowMapper(), airportUUID);
            return Optional.ofNullable(airport);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<Airport> findByAirportCode(String airportCode) {
        String sql = "SELECT * FROM airports WHERE UPPER(airport_code) = UPPER(?) AND is_deleted = false";
        try {
            Airport airport = jdbcTemplate.queryForObject(sql, new AirportRowMapper(), airportCode);
            return Optional.ofNullable(airport);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Airport> findByCity(String city) {
        String sql = "SELECT * FROM airports WHERE LOWER(city) LIKE LOWER(?) AND is_deleted = false ORDER BY city, airport_name";
        return jdbcTemplate.query(sql, new AirportRowMapper(), "%" + city + "%");
    }

    public List<Airport> findByAirportName(String name) {
        String sql = "SELECT * FROM airports WHERE LOWER(airport_name) LIKE LOWER(?) AND is_deleted = false ORDER BY airport_name";
        return jdbcTemplate.query(sql, new AirportRowMapper(), "%" + name + "%");
    }
    public List<Airport> searchAirports(String searchTerm) {
        String sql = "SELECT * FROM airports WHERE (LOWER(airport_code) LIKE LOWER(?) OR " +
                "LOWER(airport_name) LIKE LOWER(?) OR " +
                "LOWER(city) LIKE LOWER(?) OR " +
                "LOWER(country) LIKE LOWER(?)) AND is_deleted = false " +
                "ORDER BY airport_name LIMIT 50";
        String likeTerm = "%" + searchTerm + "%";
        return jdbcTemplate.query(sql, new AirportRowMapper(), likeTerm, likeTerm, likeTerm, likeTerm);
    }

    public Airport update(Airport airport) {
        String sql = "UPDATE airports SET airport_name = ?, city = ?, country = ?, timezone = ? WHERE airports_uuid = ? AND is_deleted = false";

        int rowsAffected = jdbcTemplate.update(sql,
                airport.getAirportName(),
                airport.getCity(),
                airport.getCountry(),
                airport.getTimezone(),
                airport.getAirportUUID()
        );

        if (rowsAffected == 0) {
            throw new AirportNotFoundException("Airport not found or already deleted");
        }
        return airport;
    }

    public int softDeleteByUUID(String airportUUID) {
        String sql = "UPDATE airports SET is_deleted = true, updated_at = CURRENT_TIMESTAMP " +
                "WHERE airports_uuid = ? AND is_deleted = false";

        return jdbcTemplate.update(sql, airportUUID);
    }

    public boolean existsByAirportCode(String airportCode) {
        String sql = "SELECT COUNT(*) FROM airports WHERE UPPER(airport_code) = UPPER(?) AND is_deleted = false";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, airportCode);
        return count != null && count > 0;
    }

    public boolean existsByUUIDAndNotDeleted(String airportUUID) {
        String sql = "SELECT COUNT(*) FROM airports WHERE airports_uuid = ? AND is_deleted = false";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, airportUUID);
        return count != null && count > 0;
    }

    private static class AirportRowMapper implements RowMapper<Airport> {
        @Override
        public Airport mapRow(ResultSet rs, int rowNum) throws SQLException {
            Airport airport = new Airport();
            airport.setId(rs.getLong("id"));
            airport.setAirportUUID(rs.getString("airports_uuid"));
            airport.setAirportCode(rs.getString("airport_code"));
            airport.setAirportName(rs.getString("airport_name"));
            airport.setCity(rs.getString("city"));
            airport.setCountry(rs.getString("country"));
            airport.setTimezone(rs.getString("timezone"));
            airport.setDeleted(rs.getBoolean("is_deleted"));
            airport.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            airport.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return airport;
        }
    }
}
