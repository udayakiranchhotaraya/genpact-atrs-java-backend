package com.capstone.airlineticketreservationsystem.bookings.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDTO {

    private String paymentsUUID;
    private Long bookingId;
    private String paymentMethod;
    private String paymentProvider;
    private String transactionId;
    private BigDecimal amount;
    private String currency;
    private String paymentStatus;
    private LocalDateTime paymentDate;

    public String getPaymentsUUID() { return paymentsUUID; }
    public void setPaymentsUUID(String paymentsUUID) { this.paymentsUUID = paymentsUUID; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getPaymentProvider() { return paymentProvider; }
    public void setPaymentProvider(String paymentProvider) { this.paymentProvider = paymentProvider; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public LocalDateTime getPaymentDate() { return paymentDate; }
    public void setPaymentDate(LocalDateTime paymentDate) { this.paymentDate = paymentDate; }
}
