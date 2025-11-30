package com.capstone.airlineticketreservationsystem.bookings.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RefundDTO {

    private String refundsUUID;
    private Long paymentId;
    private Long bookingId;
    private BigDecimal refundAmount;
    private BigDecimal cancellationFee;
    private BigDecimal refundableAmount;
    private String refundReason;
    private String refundStatus;
    private String refundTransactionId;
    private LocalDateTime processedAt;
    private LocalDateTime createdAt;

    public RefundDTO() {}

    public RefundDTO(String refundsUUID, Long paymentId, Long bookingId,
                     BigDecimal refundAmount, BigDecimal cancellationFee,
                     BigDecimal refundableAmount, String refundReason,
                     String refundStatus, String refundTransactionId,
                     LocalDateTime processedAt, LocalDateTime createdAt) {

        this.refundsUUID = refundsUUID;
        this.paymentId = paymentId;
        this.bookingId = bookingId;
        this.refundAmount = refundAmount;
        this.cancellationFee = cancellationFee;
        this.refundableAmount = refundableAmount;
        this.refundReason = refundReason;
        this.refundStatus = refundStatus;
        this.refundTransactionId = refundTransactionId;
        this.processedAt = processedAt;
        this.createdAt = createdAt;
    }

    // ---------------- GETTERS & SETTERS ----------------

    public String getRefundsUUID() { return refundsUUID; }
    public void setRefundsUUID(String refundsUUID) { this.refundsUUID = refundsUUID; }

    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public BigDecimal getCancellationFee() { return cancellationFee; }
    public void setCancellationFee(BigDecimal cancellationFee) { this.cancellationFee = cancellationFee; }

    public BigDecimal getRefundableAmount() { return refundableAmount; }
    public void setRefundableAmount(BigDecimal refundableAmount) { this.refundableAmount = refundableAmount; }

    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }

    public String getRefundStatus() { return refundStatus; }
    public void setRefundStatus(String refundStatus) { this.refundStatus = refundStatus; }

    public String getRefundTransactionId() { return refundTransactionId; }
    public void setRefundTransactionId(String refundTransactionId) { this.refundTransactionId = refundTransactionId; }

    public LocalDateTime getProcessedAt() { return processedAt; }
    public void setProcessedAt(LocalDateTime processedAt) { this.processedAt = processedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
