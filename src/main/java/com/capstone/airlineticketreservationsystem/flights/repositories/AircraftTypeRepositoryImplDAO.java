package com.capstone.airlineticketreservationsystem.flights.repositories;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.capstone.airlineticketreservationsystem.flights.exceptions.AircraftTypeNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.models.AircraftType;

import static com.capstone.airlineticketreservationsystem.utilities.UUIDV7Generator.generateUUIDV7;

@Repository
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
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, aircraftType.getAircraftTypeUUID());
            preparedStatement.setString(2, aircraftType.getAircraftModel());
            preparedStatement.setString(3, aircraftType.getManufacturer());
            preparedStatement.setInt(4, aircraftType.getTotalSeats());
            preparedStatement.setInt(5, aircraftType.getBusinessClassSeats());
            preparedStatement.setInt(6, aircraftType.getEconomyClassSeats());
            return preparedStatement;
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

    @Override
    public List<AircraftType> findAll() {
        String sql = "SELECT * FROM aircraft_types WHERE is_deleted = false ORDER BY manufacturer, aircraft_model";
        return jdbcTemplate.query(sql, new AircraftTypeRowMapper());
    }

    @Override
    public Optional<AircraftType> findByAircraftTypeUUID(String aircraftTypeUUID) {
        String sql = "SELECT * FROM aircraft_types WHERE aircraft_types_uuid = ? AND is_deleted = false";
        try {
            AircraftType aircraftType = jdbcTemplate.queryForObject(sql, new AircraftTypeRowMapper(), aircraftTypeUUID);
            return Optional.ofNullable(aircraftType);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public AircraftType update(AircraftType aircraftType) {
        String sql = "UPDATE aircraft_types SET aircraft_model = ?, manufacturer = ?, total_seats = ?, business_class_seats = ?, economy_class_seats = ? WHERE aircraft_types_uuid = ? AND is_deleted = false";

        int rowsAffected = jdbcTemplate.update(sql,
                aircraftType.getAircraftModel(),
                aircraftType.getManufacturer(),
                aircraftType.getTotalSeats(),
                aircraftType.getBusinessClassSeats(),
                aircraftType.getEconomyClassSeats(),
                aircraftType.getAircraftTypeUUID()
        );

        if (rowsAffected < 1) {
            throw new AircraftTypeNotFoundException("Aircraft type not found or already deleted");
        }
        return aircraftType;
    }

    @Override
    public int softDeleteByUUID(String aircraftTypeUUID) {
        String sql = "UPDATE aircraft_types SET is_deleted = true WHERE aircraft_types_uuid = ? AND is_deleted = false";

        return jdbcTemplate.update(sql, aircraftTypeUUID);
    }

    @Override
    public boolean existsByUUIDAndNotDeleted(String aircraftTypeUUID) {
        String sql = "SELECT COUNT(*) FROM aircraft_types WHERE aircraft_types_uuid = ? AND is_deleted = false";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, aircraftTypeUUID);
        return count != null && count > 0;
    }

    @Override
    public Optional<Long> findIdByUUID(String aircraftTypeUUID) {
        String sql = "SELECT id FROM aircraft_types WHERE aircraft_types_uuid = ? AND is_deleted = false";
        try {
            Long id = jdbcTemplate.queryForObject(sql, Long.class, aircraftTypeUUID);
            return Optional.ofNullable(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<AircraftType> findById(Long id) {
        String sql = "SELECT * FROM aircraft_types WHERE id = ? AND is_deleted = false";
        try {
            AircraftType aircraftType = jdbcTemplate.queryForObject(sql, new AircraftTypeRowMapper(), id);
            return Optional.ofNullable(aircraftType);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private static class AircraftTypeRowMapper implements RowMapper<AircraftType> {
        @Override
        public AircraftType mapRow(java.sql.ResultSet rs, int rowNum) throws SQLException {
            AircraftType aircraftType = new AircraftType();
            aircraftType.setId(rs.getLong("id"));
            aircraftType.setAircraftTypeUUID(rs.getString("aircraft_types_uuid"));
            aircraftType.setAircraftModel(rs.getString("aircraft_model"));
            aircraftType.setManufacturer(rs.getString("manufacturer"));
            aircraftType.setTotalSeats(rs.getInt("total_seats"));
            aircraftType.setBusinessClassSeats(rs.getInt("business_class_seats"));
            aircraftType.setEconomyClassSeats(rs.getInt("economy_class_seats"));
            aircraftType.setDeleted(rs.getBoolean("is_deleted"));
            aircraftType.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            aircraftType.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            return aircraftType;
        }
    }
}
