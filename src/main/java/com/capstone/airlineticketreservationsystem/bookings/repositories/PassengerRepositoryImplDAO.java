package com.capstone.airlineticketreservationsystem.bookings.repositories;

import com.capstone.airlineticketreservationsystem.bookings.models.Passenger;
import com.capstone.airlineticketreservationsystem.bookings.models.PassengerType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import static com.capstone.airlineticketreservationsystem.utilities.UUIDV7Generator.generateUUIDV7;

@Repository
public class PassengerRepositoryImplDAO implements PassengerRepositoryDAO {

    private final JdbcTemplate jdbcTemplate;

    public PassengerRepositoryImplDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Passenger save(Passenger passenger) {

        String sql = """
            INSERT INTO passengers (
                passengers_uuid, booking_id, user_id, first_name, last_name,email,
                date_of_birth, passport_number, nationality, passenger_type
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        passenger.setPassengersUUID(generateUUIDV7().toString());

        jdbcTemplate.update(connection -> {

            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, passenger.getPassengersUUID());
            ps.setLong(2, passenger.getBookingId());
            ps.setObject(3, passenger.getUserId());
            ps.setString(4, passenger.getFirstName());
            ps.setString(5, passenger.getLastName());
            ps.setString(6, passenger.getEmail());
            ps.setDate(7, java.sql.Date.valueOf(passenger.getDateOfBirth()));
            ps.setString(8, passenger.getPassportNumber());
            ps.setString(9, passenger.getNationality());
            ps.setString(10, passenger.getPassengerType().name());

            return ps;

        }, keyHolder);

        passenger.setId(keyHolder.getKey().longValue());
        return passenger;
    }

    @Override
    public Optional<Passenger> findByUUID(String passengersUUID) {
        try {
            String sql = "SELECT * FROM passengers WHERE passengers_uuid = ? AND is_deleted = FALSE";
            Passenger p = jdbcTemplate.queryForObject(sql, new PassengerRowMapper(), passengersUUID);
            return Optional.ofNullable(p);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Passenger> findByBookingId(Long bookingId) {
        String sql = "SELECT * FROM passengers WHERE booking_id = ? AND is_deleted = FALSE";
        return jdbcTemplate.query(sql, new PassengerRowMapper(), bookingId);
    }
    
    @Override
    public Optional<Long> findIdByUUID(String passengerUUID) {
        String sql = "SELECT id FROM passengers WHERE passengers_uuid = ? AND is_deleted = false";
        try {
            Long id = jdbcTemplate.queryForObject(sql, Long.class, passengerUUID);
            return Optional.ofNullable(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Passenger update(Passenger passenger) {
        String sql = """
            UPDATE passengers SET 
                first_name=?, last_name=?, email = ?, date_of_birth=?, passport_number=?, 
                nationality=?, passenger_type=?, updated_at=CURRENT_TIMESTAMP
            WHERE passengers_uuid=? AND is_deleted=FALSE
        """;

        jdbcTemplate.update(
                sql,
                passenger.getFirstName(),
                passenger.getLastName(),
                passenger.getEmail(), 
                passenger.getDateOfBirth(),
                passenger.getPassportNumber(),
                passenger.getNationality(),
                passenger.getPassengerType().name(),
                passenger.getPassengersUUID()
        );

        return passenger;
    }

    @Override
    public int softDelete(String passengersUUID) {
        String sql = "UPDATE passengers SET is_deleted = TRUE WHERE passengers_uuid = ?";
        return jdbcTemplate.update(sql, passengersUUID);
    }

    private static class PassengerRowMapper implements RowMapper<Passenger> {
        @Override
        public Passenger mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Passenger p = new Passenger();

            p.setId(rs.getLong("id"));
            p.setPassengersUUID(rs.getString("passengers_uuid"));
            p.setBookingId(rs.getLong("booking_id"));
            p.setUserId(rs.getObject("user_id") != null ? rs.getLong("user_id") : null);
            p.setFirstName(rs.getString("first_name"));
            p.setLastName(rs.getString("last_name"));
            p.setEmail(rs.getString("email"));
            p.setDateOfBirth(rs.getDate("date_of_birth").toLocalDate());
            p.setPassportNumber(rs.getString("passport_number"));
            p.setNationality(rs.getString("nationality"));
            p.setPassengerType(PassengerType.valueOf(rs.getString("passenger_type")));
            p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

            return p;
        }
    }
}
