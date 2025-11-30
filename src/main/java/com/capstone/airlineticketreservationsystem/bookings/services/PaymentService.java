package com.capstone.airlineticketreservationsystem.bookings.services;

import com.capstone.airlineticketreservationsystem.bookings.dtos.CreatePaymentRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.PaymentDTO;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.PaymentCreationException;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.PaymentNotFoundException;
import com.capstone.airlineticketreservationsystem.bookings.models.BookingStatus;
import com.capstone.airlineticketreservationsystem.bookings.models.Passenger;
import com.capstone.airlineticketreservationsystem.bookings.models.Payment;
import com.capstone.airlineticketreservationsystem.bookings.models.PaymentMethod;
import com.capstone.airlineticketreservationsystem.bookings.models.PaymentStatus;
import com.capstone.airlineticketreservationsystem.bookings.models.SeatClass;
import com.capstone.airlineticketreservationsystem.bookings.models.Ticket;
import com.capstone.airlineticketreservationsystem.bookings.models.TicketStatus;
import com.capstone.airlineticketreservationsystem.bookings.repositories.BookingRepositoryDAO;
import com.capstone.airlineticketreservationsystem.bookings.repositories.PassengerRepositoryDAO;
import com.capstone.airlineticketreservationsystem.bookings.repositories.PaymentRepositoryDAO;
import com.capstone.airlineticketreservationsystem.bookings.repositories.TicketRepositoryDAO;
import com.capstone.airlineticketreservationsystem.utilities.EmailService;
import com.capstone.airlineticketreservationsystem.utilities.TicketPdfGenerator;

import jakarta.mail.MessagingException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    private final PaymentRepositoryDAO paymentRepository;
    private final BookingRepositoryDAO bookingRepository;
    private final PassengerRepositoryDAO passengerRepository;
    private final TicketRepositoryDAO ticketRepository;
    private final EmailService emailService;

    public PaymentService(PaymentRepositoryDAO paymentRepository,
                          BookingRepositoryDAO bookingRepository,
                          PassengerRepositoryDAO passengerRepository,
                          TicketRepositoryDAO ticketRepository,
                          EmailService emailService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.ticketRepository = ticketRepository;
        this.emailService = emailService;
    }

    // ===================== CREATE PAYMENT =====================
    public PaymentDTO createPayment(CreatePaymentRequest req) {

        Long bookingId = bookingRepository.findByUUID(req.getBookingUUID())
                .orElseThrow(() -> new PaymentCreationException("Booking not found"))
                .getId();

        Payment payment = new Payment();

        payment.setBookingId(bookingId);
        payment.setPaymentMethod(PaymentMethod.valueOf(req.getPaymentMethod()));
        payment.setPaymentProvider(req.getPaymentProvider());
        payment.setTransactionId(req.getTransactionId());
        payment.setAmount(req.getAmount());
        payment.setCurrency(req.getCurrency());
        payment.setPaymentStatus(PaymentStatus.PENDING);

        Payment saved = paymentRepository.save(payment);
        return convertToDTO(saved);
    }

    // ===================== GET PAYMENT =====================
    public PaymentDTO getPayment(String uuid) {
        Payment p = paymentRepository.findByUUID(uuid)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));
        return convertToDTO(p);
    }

    // ===================== GET PAYMENTS BY BOOKING =====================
    public java.util.List<PaymentDTO> getPaymentsForBooking(Long bookingId) {
        return paymentRepository.findByBookingId(bookingId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ===================== CONVERTER =====================
    private PaymentDTO convertToDTO(Payment p) {
        PaymentDTO dto = new PaymentDTO();
        dto.setPaymentsUUID(p.getPaymentsUUID());
        dto.setBookingId(p.getBookingId());
        dto.setPaymentMethod(p.getPaymentMethod().name());
        dto.setPaymentProvider(p.getPaymentProvider());
        dto.setTransactionId(p.getTransactionId());
        dto.setAmount(p.getAmount());
        dto.setCurrency(p.getCurrency());
        dto.setPaymentStatus(p.getPaymentStatus().name());
        dto.setPaymentDate(p.getPaymentDate());
        return dto;
    }
    
    public void confirmPayment(String paymentUUID) {

        // 1) Fetch payment
        Payment payment = paymentRepository.findByUUID(paymentUUID)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found"));

        if (payment.getPaymentStatus() == PaymentStatus.CAPTURED) {
            throw new PaymentCreationException("Payment already confirmed.");
        }

        // 2) Mark payment successful
        payment.setPaymentStatus(PaymentStatus.CAPTURED);
        paymentRepository.update(payment);

        // 3) Fetch booking
        var booking = bookingRepository.findById(payment.getBookingId())
                .orElseThrow(() -> new PaymentCreationException("Booking not found for payment"));

        // 4) Fetch all passengers under that booking
        List<Passenger> passengers = passengerRepository.findByBookingId(payment.getBookingId());

//         5) Generate tickets for every passenger
        
        for (Passenger p : passengers) {

            Ticket ticket = new Ticket();
            ticket.setTicketNumber(UUID.randomUUID().toString().replace("-", "").substring(0, 12));
            ticket.setBookingId(payment.getBookingId());
            ticket.setPassengerId(p.getId());

            ticket.setFlightId(booking.getFlightId());   // IMPORTANT: booking must have flightId
            ticket.setSeatClass(booking.getSeatClass());      // default – upgrade later if needed

            ticket.setBaseFare(BigDecimal.valueOf(3000)); // placeholder
            ticket.setTaxes(BigDecimal.valueOf(300));     // placeholder
            ticket.setAncillaryCharges(BigDecimal.ZERO);

            ticket.setTotalFare(ticket.getBaseFare().add(ticket.getTaxes()));
            ticket.setTicketStatus(TicketStatus.ISSUED);

            Ticket saved = ticketRepository.save(ticket);
         // 1. Generate PDF
            byte[] pdf = TicketPdfGenerator.generateTicketPdf(saved);

            // 2. Fetch passenger email
            String email = p.getEmail(); // make sure passenger model has email

            // 3. Send PDF via email
            try {
				emailService.sendTicketEmail(email, p.getFirstName(), pdf);
			} catch (MessagingException e) {
				throw new RuntimeException("Failed to send ticket email", e);
			}
        }

        // 6) Update booking status → CONFIRMED
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        bookingRepository.update(booking);
    }

    
}
