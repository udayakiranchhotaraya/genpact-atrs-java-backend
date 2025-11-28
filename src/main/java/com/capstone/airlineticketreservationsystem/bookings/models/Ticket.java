package com.capstone.airlineticketreservationsystem.bookings.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Ticket {

    public Ticket() {
    }

    public Ticket(String ticketNumber, Long bookingId, Long passengerId, Long flightId, SeatClass seatClass, BigDecimal baseFare, BigDecimal taxes, BigDecimal totalFare) {
        this.ticketNumber = ticketNumber;
        this.bookingId = bookingId;
        this.passengerId = passengerId;
        this.flightId = flightId;
        this.seatClass = seatClass;
        this.baseFare = baseFare;
        this.taxes = taxes;
        this.ancillaryCharges = BigDecimal.ZERO;
        this.totalFare = totalFare;
        this.ticketStatus = TicketStatus.ISSUED;
    }

    private Long id;
    private String ticketsUUID;
    private String ticketNumber;
    private Long bookingId;
    private Long passengerId;
    private Long flightId;
    private SeatClass seatClass;
    private BigDecimal baseFare;
    private BigDecimal taxes;
    private BigDecimal ancillaryCharges;
    private BigDecimal totalFare;
    private TicketStatus ticketStatus;
    private LocalDateTime checkedInAt;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketsUUID() {
        return ticketsUUID;
    }

    public void setTicketsUUID(String ticketsUUID) {
        this.ticketsUUID = ticketsUUID;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Long getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(Long passengerId) {
        this.passengerId = passengerId;
    }

    public Long getFlightId() {
        return flightId;
    }

    public void setFlightId(Long flightId) {
        this.flightId = flightId;
    }

    public SeatClass getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(SeatClass seatClass) {
        this.seatClass = seatClass;
    }

    public BigDecimal getBaseFare() {
        return baseFare;
    }

    public void setBaseFare(BigDecimal baseFare) {
        this.baseFare = baseFare;
    }

    public BigDecimal getTaxes() {
        return taxes;
    }

    public void setTaxes(BigDecimal taxes) {
        this.taxes = taxes;
    }

    public BigDecimal getAncillaryCharges() {
        return ancillaryCharges;
    }

    public void setAncillaryCharges(BigDecimal ancillaryCharges) {
        this.ancillaryCharges = ancillaryCharges;
    }

    public BigDecimal getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(BigDecimal totalFare) {
        this.totalFare = totalFare;
    }

    public TicketStatus getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(TicketStatus ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public LocalDateTime getCheckedInAt() {
        return checkedInAt;
    }

    public void setCheckedInAt(LocalDateTime checkedInAt) {
        this.checkedInAt = checkedInAt;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
