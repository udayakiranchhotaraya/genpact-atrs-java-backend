package com.capstone.airlineticketreservationsystem.bookings.repositories;

import com.capstone.airlineticketreservationsystem.bookings.models.SeatClass;
import com.capstone.airlineticketreservationsystem.bookings.models.Ticket;
import com.capstone.airlineticketreservationsystem.bookings.models.TicketStatus;
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
public class TicketRepositoryImplDAO implements TicketRepositoryDAO {

    private final JdbcTemplate jdbcTemplate;

    public TicketRepositoryImplDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Ticket save(Ticket ticket) {
        String sql = """
                INSERT INTO tickets (
                    tickets_uuid, ticket_number, booking_id, passenger_id, flight_id,
                    seat_class, base_fare, taxes, ancillary_charges, total_fare,
                    ticket_status, checked_in_at
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        if (ticket.getTicketsUUID() == null) {
            ticket.setTicketsUUID(generateUUIDV7().toString());
        }

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, ticket.getTicketsUUID());
            ps.setString(2, ticket.getTicketNumber());
            ps.setLong(3, ticket.getBookingId());
            ps.setLong(4, ticket.getPassengerId());
            ps.setLong(5, ticket.getFlightId());
            ps.setString(6, ticket.getSeatClass().name());
            ps.setBigDecimal(7, ticket.getBaseFare());
            ps.setBigDecimal(8, ticket.getTaxes());
            ps.setBigDecimal(9, ticket.getAncillaryCharges());
            ps.setBigDecimal(10, ticket.getTotalFare());
            ps.setString(11, ticket.getTicketStatus().name());

            if (ticket.getCheckedInAt() != null)
                ps.setTimestamp(12, java.sql.Timestamp.valueOf(ticket.getCheckedInAt()));
            else
                ps.setTimestamp(12, null);

            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            ticket.setId(keyHolder.getKey().longValue());
        }

        return ticket;
    }

    @Override
    public Optional<Ticket> findByUUID(String ticketsUUID) {
        try {
            String sql = "SELECT * FROM tickets WHERE tickets_uuid = ? AND is_deleted = false";
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, new TicketRowMapper(), ticketsUUID)
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Ticket> findByTicketNumber(String ticketNumber) {
        try {
            String sql = "SELECT * FROM tickets WHERE ticket_number = ? AND is_deleted = false";
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(sql, new TicketRowMapper(), ticketNumber)
            );
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Ticket> findByBookingId(Long bookingId) {
        String sql = "SELECT * FROM tickets WHERE booking_id = ? AND is_deleted = false";
        return jdbcTemplate.query(sql, new TicketRowMapper(), bookingId);
    }

    @Override
    public List<Ticket> findByPassengerId(Long passengerId) {
        String sql = "SELECT * FROM tickets WHERE passenger_id = ? AND is_deleted = false";
        return jdbcTemplate.query(sql, new TicketRowMapper(), passengerId);
    }

    @Override
    public List<Ticket> findByFlightId(Long flightId) {
        String sql = "SELECT * FROM tickets WHERE flight_id = ? AND is_deleted = false";
        return jdbcTemplate.query(sql, new TicketRowMapper(), flightId);
    }

    @Override
    public Ticket update(Ticket ticket) {
        String sql = """
                UPDATE tickets SET
                    ticket_status = ?, checked_in_at = ?, ancillary_charges = ?, 
                    total_fare = ?, updated_at = CURRENT_TIMESTAMP
                WHERE tickets_uuid = ? AND is_deleted = false
                """;

        jdbcTemplate.update(sql,
                ticket.getTicketStatus().name(),
                ticket.getCheckedInAt() != null ?
                        java.sql.Timestamp.valueOf(ticket.getCheckedInAt()) : null,
                ticket.getAncillaryCharges(),
                ticket.getTotalFare(),
                ticket.getTicketsUUID()
        );

        return ticket;
    }

    @Override
    public int softDeleteByUUID(String ticketsUUID) {
        String sql = "UPDATE tickets SET is_deleted = true WHERE tickets_uuid = ? AND is_deleted = false";
        return jdbcTemplate.update(sql, ticketsUUID);
    }

    // ------------------ RowMapper ---------------------

    private static class TicketRowMapper implements RowMapper<Ticket> {
        @Override
        public Ticket mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {

            Ticket t = new Ticket();
            t.setId(rs.getLong("id"));
            t.setTicketsUUID(rs.getString("tickets_uuid"));
            t.setTicketNumber(rs.getString("ticket_number"));
            t.setBookingId(rs.getLong("booking_id"));
            t.setPassengerId(rs.getLong("passenger_id"));
            t.setFlightId(rs.getLong("flight_id"));
            t.setSeatClass(SeatClass.valueOf(rs.getString("seat_class")));
            t.setBaseFare(rs.getBigDecimal("base_fare"));
            t.setTaxes(rs.getBigDecimal("taxes"));
            t.setAncillaryCharges(rs.getBigDecimal("ancillary_charges"));
            t.setTotalFare(rs.getBigDecimal("total_fare"));
            t.setTicketStatus(TicketStatus.valueOf(rs.getString("ticket_status")));

            java.sql.Timestamp checkInTs = rs.getTimestamp("checked_in_at");
            if (checkInTs != null) {
                t.setCheckedInAt(checkInTs.toLocalDateTime());
            }

            t.setDeleted(rs.getBoolean("is_deleted"));
            t.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            t.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());

            return t;
        }
    }
}
