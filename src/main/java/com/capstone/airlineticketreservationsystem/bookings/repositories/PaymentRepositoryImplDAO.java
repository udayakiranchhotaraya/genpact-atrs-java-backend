package com.capstone.airlineticketreservationsystem.bookings.repositories;

import com.capstone.airlineticketreservationsystem.bookings.models.Payment;
import com.capstone.airlineticketreservationsystem.bookings.models.PaymentMethod;
import com.capstone.airlineticketreservationsystem.bookings.models.PaymentStatus;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static com.capstone.airlineticketreservationsystem.utilities.UUIDV7Generator.generateUUIDV7;

@Repository
public class PaymentRepositoryImplDAO implements PaymentRepositoryDAO {

    private final JdbcTemplate jdbcTemplate;

    public PaymentRepositoryImplDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ================= SAVE PAYMENT =================
    @Override
    public Payment save(Payment payment) {

        String sql = """
                INSERT INTO payments (
                    payments_uuid, booking_id, payment_method, payment_provider,
                    transaction_id, amount, currency, payment_status
                ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        payment.setPaymentsUUID(generateUUIDV7().toString());

        jdbcTemplate.update(connection -> {
            PreparedStatement ps =
                    connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, payment.getPaymentsUUID());
            ps.setLong(2, payment.getBookingId());
            ps.setString(3, payment.getPaymentMethod().name());
            ps.setString(4, payment.getPaymentProvider());
            ps.setString(5, payment.getTransactionId());
            ps.setBigDecimal(6, payment.getAmount());
            ps.setString(7, payment.getCurrency());
            ps.setString(8, payment.getPaymentStatus().name());

            return ps;
        }, keyHolder);

        Number key = keyHolder.getKey();
        if (key != null) {
            payment.setId(key.longValue());

            String timeSql = "SELECT created_at FROM payments WHERE id = ?";
            return jdbcTemplate.queryForObject(timeSql, (rs, row) -> {
                payment.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                return payment;
            }, key.longValue());
        }

        throw new RuntimeException("Failed to save payment.");
    }

    // ================= FIND BY UUID =================
    @Override
    public Optional<Payment> findByUUID(String paymentsUUID) {
        String sql = "SELECT * FROM payments WHERE payments_uuid = ? AND is_deleted = false";

        try {
            Payment p = jdbcTemplate.queryForObject(sql, new PaymentRowMapper(), paymentsUUID);
            return Optional.ofNullable(p);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // ================= FIND BY ID =================
    @Override
    public Optional<Payment> findById(Long id) {
        String sql = "SELECT * FROM payments WHERE id = ? AND is_deleted = false";
        try {
            Payment p = jdbcTemplate.queryForObject(sql, new PaymentRowMapper(), id);
            return Optional.ofNullable(p);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // ================= FIND ALL BY BOOKING =================
    @Override
    public List<Payment> findByBookingId(Long bookingId) {
        String sql = "SELECT * FROM payments WHERE booking_id = ? AND is_deleted = false";
        return jdbcTemplate.query(sql, new PaymentRowMapper(), bookingId);
    }

    // ================= UPDATE PAYMENT =================
    @Override
    public Payment update(Payment payment) {

        String sql = """
                UPDATE payments SET
                    payment_method = ?,
                    payment_provider = ?,
                    transaction_id = ?,
                    amount = ?,
                    currency = ?,
                    payment_status = ?,
                    updated_at = CURRENT_TIMESTAMP
                WHERE payments_uuid = ? AND is_deleted = false
                """;

        jdbcTemplate.update(sql,
                payment.getPaymentMethod().name(),
                payment.getPaymentProvider(),
                payment.getTransactionId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getPaymentStatus().name(),
                payment.getPaymentsUUID()
        );

        return payment;
    }

    // ================= SOFT DELETE =================
    @Override
    public int softDeleteByUUID(String paymentsUUID) {
        String sql = "UPDATE payments SET is_deleted = true WHERE payments_uuid = ? AND is_deleted = false";
        return jdbcTemplate.update(sql, paymentsUUID);
    }

    // ================= ROW MAPPER =================
    private static class PaymentRowMapper implements RowMapper<Payment> {

        @Override
        public Payment mapRow(ResultSet rs, int rowNum) throws SQLException {

            Payment p = new Payment();

            p.setId(rs.getLong("id"));
            p.setPaymentsUUID(rs.getString("payments_uuid"));
            p.setBookingId(rs.getLong("booking_id"));

            // ENUM FIX
            p.setPaymentMethod(PaymentMethod.valueOf(rs.getString("payment_method")));

            p.setPaymentProvider(rs.getString("payment_provider"));
            p.setTransactionId(rs.getString("transaction_id"));
            p.setAmount(rs.getBigDecimal("amount"));
            p.setCurrency(rs.getString("currency"));

            // ENUM FIX
            p.setPaymentStatus(PaymentStatus.valueOf(rs.getString("payment_status")));

            p.setPaymentDate(rs.getTimestamp("payment_date").toLocalDateTime());
            p.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            p.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
            p.setDeleted(rs.getBoolean("is_deleted"));

            return p;
        }
    }
}
