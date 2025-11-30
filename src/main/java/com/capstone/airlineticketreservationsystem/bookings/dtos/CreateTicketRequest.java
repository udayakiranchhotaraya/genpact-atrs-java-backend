package com.capstone.airlineticketreservationsystem.bookings.dtos;

import com.capstone.airlineticketreservationsystem.bookings.models.SeatClass;
import java.math.BigDecimal;

public class CreateTicketRequest {

    private Long bookingId;
    private Long passengerId;
    private Long flightId;
    private SeatClass seatClass;   // ✅ CHANGED to enum
    private BigDecimal baseFare;
    private BigDecimal taxes;

    public Long getBookingId() { return bookingId; }
    public void setBookingId(Long bookingId) { this.bookingId = bookingId; }

    public Long getPassengerId() { return passengerId; }
    public void setPassengerId(Long passengerId) { this.passengerId = passengerId; }

    public Long getFlightId() { return flightId; }
    public void setFlightId(Long flightId) { this.flightId = flightId; }

    public SeatClass getSeatClass() { return seatClass; }
    public void setSeatClass(SeatClass seatClass) { this.seatClass = seatClass; }  // ✅ enum setter

    public BigDecimal getBaseFare() { return baseFare; }
    public void setBaseFare(BigDecimal baseFare) { this.baseFare = baseFare; }

    public BigDecimal getTaxes() { return taxes; }
    public void setTaxes(BigDecimal taxes) { this.taxes = taxes; }
}
