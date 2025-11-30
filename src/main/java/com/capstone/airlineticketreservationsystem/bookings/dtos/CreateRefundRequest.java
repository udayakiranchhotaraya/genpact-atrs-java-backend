package com.capstone.airlineticketreservationsystem.bookings.dtos;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public class CreateRefundRequest {

    @NotNull
    private Long paymentId;

    @NotNull
    private String bookingUUID;

    @NotNull
    private BigDecimal refundAmount;

    private BigDecimal cancellationFee;
    private String refundReason;

    // Getters & Setters
    public Long getPaymentId() { return paymentId; }
    public void setPaymentId(Long paymentId) { this.paymentId = paymentId; }

    public String getBookingUUID() { return bookingUUID; }
    public void setBookingUUId(String bookingUUID) { this.bookingUUID= bookingUUID; }

    public BigDecimal getRefundAmount() { return refundAmount; }
    public void setRefundAmount(BigDecimal refundAmount) { this.refundAmount = refundAmount; }

    public BigDecimal getCancellationFee() { return cancellationFee; }
    public void setCancellationFee(BigDecimal cancellationFee) { this.cancellationFee = cancellationFee; }

    public String getRefundReason() { return refundReason; }
    public void setRefundReason(String refundReason) { this.refundReason = refundReason; }
}
