package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

    @Override
    public List<Airline> findAll() {
        String sql = "SELECT * FROM airlines WHERE is_deleted = false ORDER BY airline_name";
        return jdbcTemplate.query(sql, new AirlineRowMapper());
    }

    @Override
    public Optional<Airline> findByAirlineUUID(String airlineUuid) {
        String sql = "SELECT * FROM airlines WHERE airlines_uuid = ? AND is_deleted = false";
        try {
            Airline airline = jdbcTemplate.queryForObject(sql, new AirlineRowMapper(), airlineUuid);
            return Optional.ofNullable(airline);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Airline> findByAirlineCode(String airlineCode) {
        String sql = "SELECT * FROM airlines WHERE airline_code = ? AND is_deleted = false";
        try {
            Airline airline = jdbcTemplate.queryForObject(sql, new AirlineRowMapper(), airlineCode);
            return Optional.ofNullable(airline);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Airline update(Airline airline) {
        String sql = "UPDATE airlines SET airline_name = ?, country = ?, logo_url = ?, updated_at = CURRENT_TIMESTAMP WHERE airlines_uuid = ?";

        jdbcTemplate.update(sql,
                airline.getAirlineName(),
                airline.getCountry(),
                airline.getLogoURL(),
                airline.getAirlineUUID()
        );
        return airline;
    }

    public boolean existsByAirlineCode(String airlineCode) {
        String sql = "SELECT COUNT(*) FROM airlines WHERE airline_code = ? AND is_deleted = false";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, airlineCode);
        return (count != null && count > 0);
    }

    private static class AirlineRowMapper implements RowMapper<Airline> {
        @Override
        public Airline mapRow(ResultSet rs, int rowNum) throws SQLException {
            Airline airline = new Airline();
            airline.setId(rs.getLong("id"));
            airline.setAirlineUUID(rs.getString("airlines_uuid"));
            airline.setAirlineCode(rs.getString("airline_code"));
            airline.setAirlineName(rs.getString("airline_name"));
            airline.setCountry(rs.getString("country"));
            airline.setLogoURL(rs.getString("logo_url"));
            airline.setDeleted(rs.getBoolean("is_deleted"));
            airline.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            airline.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return airline;
        }
    }
}
