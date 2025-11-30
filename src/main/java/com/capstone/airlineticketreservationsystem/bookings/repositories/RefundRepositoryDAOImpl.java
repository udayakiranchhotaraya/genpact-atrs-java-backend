package com.capstone.airlineticketreservationsystem.bookings.repositories;

import com.capstone.airlineticketreservationsystem.bookings.models.Refund;
import com.capstone.airlineticketreservationsystem.bookings.models.Refund.RefundStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import static com.capstone.airlineticketreservationsystem.utilities.UUIDV7Generator.generateUUIDV7;

@Repository
public class RefundRepositoryDAOImpl implements RefundRepositoryDAO {

    private final JdbcTemplate jdbcTemplate;

    public RefundRepositoryDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Refund save(Refund refund) {
        String sql = """
            INSERT INTO refunds (
            refunds_uuid, payment_id, booking_id,
            refund_amount, cancellation_fee, refundable_amount,
            refund_reason, refund_status, refund_transaction_id
            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

        if (refund.getRefundsUUID() == null)
            refund.setRefundsUUID(generateUUIDV7().toString());

        jdbcTemplate.update(sql,
                refund.getRefundsUUID(),
                refund.getPaymentId(),
                refund.getBookingId(),
                refund.getRefundAmount(),
                refund.getCancellationFee(),
                refund.getRefundableAmount(),
                refund.getRefundReason(),
                refund.getRefundStatus().name(),
                refund.getRefundTransactionId()
        );

        return refund;
    }

    @Override
    public Optional<Refund> findByUUID(String refundsUUID) {
        try {
            Refund refund = jdbcTemplate.queryForObject(
                    "SELECT * FROM refunds WHERE refunds_uuid = ? AND is_deleted = false",
                    refundRowMapper, refundsUUID
            );
            return Optional.ofNullable(refund);

        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Refund> findByBookingId(Long bookingId) {
        return jdbcTemplate.query(
                "SELECT * FROM refunds WHERE booking_id = ? AND is_deleted = false",
                refundRowMapper, bookingId
        );
    }

    @Override
    public List<Refund> findAll() {
        return jdbcTemplate.query(
                "SELECT * FROM refunds WHERE is_deleted = false",
                refundRowMapper
        );
    }

    @Override
    public Refund update(Refund refund) {
        String sql = """
            UPDATE refunds SET
            refund_status = ?, refund_transaction_id = ?, processed_at = CURRENT_TIMESTAMP
            WHERE refunds_uuid = ?
        """;

        jdbcTemplate.update(sql,
                refund.getRefundStatus().name(),
                refund.getRefundTransactionId(),
                refund.getRefundsUUID()
        );

        return refund;
    }

    @Override
    public int softDelete(String refundsUUID) {
        return jdbcTemplate.update(
                "UPDATE refunds SET is_deleted = true WHERE refunds_uuid = ?",
                refundsUUID
        );
    }

    private final RowMapper<Refund> refundRowMapper = (rs, rowNum) -> {
        Refund r = new Refund();

        r.setId(rs.getLong("id"));
        r.setRefundsUUID(rs.getString("refunds_uuid"));
        r.setPaymentId(rs.getLong("payment_id"));
        r.setBookingId(rs.getLong("booking_id"));
        r.setRefundAmount(rs.getBigDecimal("refund_amount"));
        r.setCancellationFee(rs.getBigDecimal("cancellation_fee"));
        r.setRefundableAmount(rs.getBigDecimal("refundable_amount"));
        r.setRefundReason(rs.getString("refund_reason"));
        r.setRefundStatus(RefundStatus.valueOf(rs.getString("refund_status")));
        r.setRefundTransactionId(rs.getString("refund_transaction_id"));

        Timestamp processedTs = rs.getTimestamp("processed_at");
        r.setProcessedAt(processedTs != null ? processedTs.toLocalDateTime() : null);

        r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        r.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
        r.setDeleted(rs.getBoolean("is_deleted"));

        return r;
    };
}
