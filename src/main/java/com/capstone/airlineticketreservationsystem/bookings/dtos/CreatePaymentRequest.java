package com.capstone.airlineticketreservationsystem.bookings.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class CreatePaymentRequest {

    @NotNull(message = "bookingUUID is required")
    private String bookingUUID;

    @NotNull(message = "paymentMethod is required")
    private String paymentMethod;   // CREDIT_CARD / UPI / DEBIT_CARD

    private String paymentProvider; // Razorpay / PayPal / null

    private String transactionId;   // Gateway transaction ID (optional)

    @Positive(message = "Amount must be positive")
    private BigDecimal amount;

    private String currency = "INR"; // default

    public String getBookingUUID() { return bookingUUID; }
    public void setBookingUUID(String bookingUUID) { this.bookingUUID = bookingUUID; }

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
}
