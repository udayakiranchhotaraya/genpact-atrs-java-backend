package com.capstone.airlineticketreservationsystem.bookings.repositories;

import com.capstone.airlineticketreservationsystem.bookings.models.Booking;
import com.capstone.airlineticketreservationsystem.bookings.models.BookingStatus;
import com.capstone.airlineticketreservationsystem.bookings.models.SeatClass;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.BookingNotFoundException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import static com.capstone.airlineticketreservationsystem.utilities.UUIDV7Generator.generateUUIDV7;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

@Repository
public class BookingRepositoryImplDAO implements BookingRepositoryDAO {

    private final JdbcTemplate jdbcTemplate;

    public BookingRepositoryImplDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ===========================================
    // SAVE BOOKING  (seat_class added)
    // ===========================================
    @Override
    public Booking save(Booking booking) {

        String sql = """
            INSERT INTO bookings (
                bookings_uuid, pnr, user_id, flight_id, seat_class, booking_status, is_deleted
            ) VALUES (?, ?, ?, ?, ?, ?, FALSE)
        """;

        booking.setBookingsUUID(generateUUIDV7().toString());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, booking.getBookingsUUID());
            ps.setString(2, booking.getPnr());
            ps.setLong(3, booking.getUserId());
            ps.setLong(4, booking.getFlightId());
            ps.setString(5, booking.getSeatClass().name()); // ⭐ added
            ps.setString(6, booking.getBookingStatus().name());

            return ps;
        }, keyHolder);

        booking.setId(keyHolder.getKey().longValue());

        return jdbcTemplate.queryForObject(
                "SELECT * FROM bookings WHERE id = ?",
                new BookingRowMapper(),
                booking.getId()
        );
    }

    // ===========================================
    // UPDATE BOOKING (already correct)
    // ===========================================
    @Override
    public Booking update(Booking booking) {

        String sql = """
                UPDATE bookings SET
                    pnr = ?, 
                    booking_status = ?, 
                    seat_class = ?, 
                    flight_id = ?, 
                    is_deleted = ?, 
                    updated_at = CURRENT_TIMESTAMP
                WHERE bookings_uuid = ?
        """;

        int rows = jdbcTemplate.update(sql,
                booking.getPnr(),
                booking.getBookingStatus().name(),
                booking.getSeatClass().name(),
                booking.getFlightId(),
                booking.getDeleted(),
                booking.getBookingsUUID()
        );

        if (rows == 0)
            throw new BookingNotFoundException("Booking update failed.");

        return findByUUID(booking.getBookingsUUID()).orElseThrow();
    }

    @Override
    public int softDeleteByUUID(String bookingUUID) {
        return jdbcTemplate.update(
                "UPDATE bookings SET is_deleted = true WHERE bookings_uuid = ?",
                bookingUUID
        );
    }

    @Override
    public Optional<Booking> findByUUID(String bookingUUID) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM bookings WHERE bookings_uuid = ? AND is_deleted = false",
                            new BookingRowMapper(),
                            bookingUUID
                    )
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Long> findIdByUUID(String bookingUUID) {
        try {
            Long id = jdbcTemplate.queryForObject(
                    "SELECT id FROM bookings WHERE bookings_uuid = ?",
                    Long.class,
                    bookingUUID
            );
            return Optional.ofNullable(id);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Booking> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM bookings WHERE is_deleted = false ORDER BY created_at DESC",
                new BookingRowMapper()
        );
    }

    // ===========================================
    // ROW MAPPER (seat_class added)
    // ===========================================
    private static class BookingRowMapper implements RowMapper<Booking> {
        @Override
        public Booking mapRow(java.sql.ResultSet rs, int rowNum) throws SQLException {

            Booking booking = new Booking();

            booking.setId(rs.getLong("id"));
            booking.setBookingsUUID(rs.getString("bookings_uuid"));
            booking.setPnr(rs.getString("pnr"));
            booking.setUserId(rs.getLong("user_id"));
            booking.setFlightId(rs.getLong("flight_id"));
            booking.setSeatClass(SeatClass.valueOf(rs.getString("seat_class"))); // ⭐ added
            booking.setBookingStatus(BookingStatus.valueOf(rs.getString("booking_status")));
            booking.setDeleted(rs.getBoolean("is_deleted"));
            booking.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            booking.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

            return booking;
        }
    }

    @Override
    public int expirePendingBookings(int minutes) {

        // 1) Fetch all expired pending bookings
        String fetchSql = """
            SELECT id, flight_id, seat_class 
            FROM bookings
            WHERE booking_status = 'PENDING'
              AND created_at < (NOW() - INTERVAL ? MINUTE)
              AND is_deleted = false
        """;

        List<Booking> expired = jdbcTemplate.query(fetchSql, (rs, row) -> {
            Booking b = new Booking();
            b.setId(rs.getLong("id"));
            b.setFlightId(rs.getLong("flight_id"));
            b.setSeatClass(SeatClass.valueOf(rs.getString("seat_class")));
            return b;
        }, minutes);

        // 2) Release seats back to aircraft_types
        String releaseSql = """
            UPDATE aircraft_types a
            JOIN flights f ON f.aircraft_type_id = a.id
            SET 
                a.economy_class_seats  = a.economy_class_seats  + (CASE WHEN ? = 'ECONOMY' THEN 1 ELSE 0 END),
                a.business_class_seats = a.business_class_seats + (CASE WHEN ? = 'BUSINESS' THEN 1 ELSE 0 END)
            WHERE f.id = ?
        """;

        for (Booking b : expired) {
            jdbcTemplate.update(
                    releaseSql,
                    b.getSeatClass().name(),
                    b.getSeatClass().name(),
                    b.getFlightId()
            );
        }

        // 3) Mark bookings as CANCELLED
        String cancelSql = """
            UPDATE bookings
            SET booking_status = 'CANCELLED',
                updated_at = CURRENT_TIMESTAMP
            WHERE booking_status = 'PENDING'
              AND created_at < (NOW() - INTERVAL ? MINUTE)
              AND is_deleted = false
        """;

        return jdbcTemplate.update(cancelSql, minutes);
    }


    @Override
    public Optional<Booking> findById(Long id) {
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM bookings WHERE id = ? AND is_deleted = false",
                            new BookingRowMapper(),
                            id
                    )
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Booking> findAllBookingsByUserId(Long userId) {
        String sql = "SELECT * FROM bookings WHERE user_id = ? AND is_deleted = false ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BookingRowMapper(), userId);
    }
}
