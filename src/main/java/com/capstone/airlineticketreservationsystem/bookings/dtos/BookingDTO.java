package com.capstone.airlineticketreservationsystem.bookings.dtos;

import com.capstone.airlineticketreservationsystem.bookings.models.BookingStatus;

import java.time.LocalDateTime;

public class BookingDTO {

    private String bookingUUID;
    private String pnr;
    private String userUUID;
    private BookingStatus bookingStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String seatClass;

    public BookingDTO() {}

    public BookingDTO(String bookingUUID, String pnr, String userUUID,
                      BookingStatus bookingStatus, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.bookingUUID = bookingUUID;
        this.pnr = pnr;
        this.userUUID = userUUID;
        this.bookingStatus = bookingStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getBookingUUID() { return bookingUUID; }
    public void setBookingUUID(String bookingUUID) { this.bookingUUID = bookingUUID; }

    public String getPnr() { return pnr; }
    public void setPnr(String pnr) { this.pnr = pnr; }

    public String getUserUUID() { return userUUID; }
    public void setUserUUID(String userUUID) { this.userUUID = userUUID; }

    public BookingStatus getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(BookingStatus bookingStatus) { this.bookingStatus = bookingStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    public String getSeatClass() {
        return seatClass;
    }

    public void setSeatClass(String seatClass) {
        this.seatClass = seatClass;
    }
}
