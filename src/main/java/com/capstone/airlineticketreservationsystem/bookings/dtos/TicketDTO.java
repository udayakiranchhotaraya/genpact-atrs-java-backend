package com.capstone.airlineticketreservationsystem.bookings.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketDTO {

    private String ticketsUUID;
    private String ticketNumber;
    private Long bookingId;
    private Long passengerId;
    private Long flightId;
    private String seatClass;   // Enum → stored as String in DTO
    private BigDecimal baseFare;
    private BigDecimal taxes;
    private BigDecimal totalFare;
    private String ticketStatus;
    private LocalDateTime createdAt;

    public String getTicketsUUID() { return ticketsUUID; }
    public void setTicketsUUID(String ticketsUUID) { this.ticketsUUID = ticketsUUID; }

    public String getTicketNumber() { return ticketNumber; }
    public void setTicketNumber(String ticketNumber) { this.ticketNumber = ticketNumber; }

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }

    public String getSeatClass() { return seatClass; }
    public void setSeatClass(String seatClass) { this.seatClass = seatClass; }

    public BigDecimal getBaseFare() { return baseFare; }
    public void setBaseFare(BigDecimal baseFare) { this.baseFare = baseFare; }

    public BigDecimal getTaxes() { return taxes; }
    public void setTaxes(BigDecimal taxes) { this.taxes = taxes; }

    public BigDecimal getTotalFare() { return totalFare; }
    public void setTotalFare(BigDecimal totalFare) { this.totalFare = totalFare; }

    public String getTicketStatus() { return ticketStatus; }
    public void setTicketStatus(String ticketStatus) { this.ticketStatus = ticketStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
