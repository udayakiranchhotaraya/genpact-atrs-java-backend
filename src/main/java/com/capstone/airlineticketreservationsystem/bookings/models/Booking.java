package com.capstone.airlineticketreservationsystem.bookings.models;

import java.time.LocalDateTime;

public class Booking {

    public Booking() {}

    public Booking(String pnr, Long userId, SeatClass seatClass) {
        this.pnr = pnr;
        this.userId = userId;
        this.seatClass = seatClass;     
    }

    private Long id;
    private String bookingsUUID;
    private String pnr;
    private Long userId;
    private BookingStatus bookingStatus;
    private Boolean isDeleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long flightId;
    private SeatClass seatClass;   

    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getBookingsUUID() { return bookingsUUID; }
    public void setBookingsUUID(String bookingsUUID) { this.bookingsUUID = bookingsUUID; }

    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }

    public Boolean getDeleted() { return isDeleted; }
    public void setDeleted(Boolean deleted) { isDeleted = deleted; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public SeatClass getSeatClass() { return seatClass; }
    public void setSeatClass(SeatClass seatClass) { this.seatClass = seatClass; }
}
