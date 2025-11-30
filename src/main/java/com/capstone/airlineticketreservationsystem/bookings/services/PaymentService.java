package com.capstone.airlineticketreservationsystem.bookings.services;

import com.capstone.airlineticketreservationsystem.bookings.dtos.CreatePaymentRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.CreateTicketRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.PaymentDTO;
import com.capstone.airlineticketreservationsystem.bookings.dtos.TicketDTO;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.BookingNotFoundException;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.PaymentCreationException;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.PaymentNotFoundException;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.TicketNotFoundException;
import com.capstone.airlineticketreservationsystem.bookings.models.*;
import com.capstone.airlineticketreservationsystem.bookings.repositories.BookingRepositoryDAO;
import com.capstone.airlineticketreservationsystem.bookings.repositories.PassengerRepositoryDAO;
import com.capstone.airlineticketreservationsystem.bookings.repositories.PaymentRepositoryDAO;
import com.capstone.airlineticketreservationsystem.bookings.repositories.TicketRepositoryDAO;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AirlineNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.exceptions.AirportNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.exceptions.FlightNotFoundException;
import com.capstone.airlineticketreservationsystem.flights.models.Airline;
import com.capstone.airlineticketreservationsystem.flights.models.Airport;
import com.capstone.airlineticketreservationsystem.flights.models.Flight;
import com.capstone.airlineticketreservationsystem.flights.repositories.AirlineRepositoryDAO;
import com.capstone.airlineticketreservationsystem.flights.repositories.AirportRepositoryDAO;
import com.capstone.airlineticketreservationsystem.flights.repositories.FlightRepositoryDAO;
import com.capstone.airlineticketreservationsystem.users.exceptions.UserNotFoundException;
import com.capstone.airlineticketreservationsystem.users.models.User;
import com.capstone.airlineticketreservationsystem.users.repositories.UserRepositoryDAO;
import com.capstone.airlineticketreservationsystem.utilities.EmailService;
import com.capstone.airlineticketreservationsystem.utilities.TicketPdfGenerator;

import jakarta.mail.MessagingException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import static com.capstone.airlineticketreservationsystem.utilities.TicketPdfGenerator.generateTicketPdf;

@Service
public class PaymentService {

    private final PaymentRepositoryDAO paymentRepository;
    private final BookingRepositoryDAO bookingRepository;
    private final PassengerRepositoryDAO passengerRepository;
    private final TicketRepositoryDAO ticketRepository;
    private final EmailService emailService;
    
    private final FlightRepositoryDAO flightRepository;
    private final AirlineRepositoryDAO airlineRepository;
    private final AirportRepositoryDAO airportRepository;
    private final UserRepositoryDAO userRepository;
    private final TicketService ticketService;

    public PaymentService(PaymentRepositoryDAO paymentRepository,
                          BookingRepositoryDAO bookingRepository,
                          PassengerRepositoryDAO passengerRepository,
                          TicketRepositoryDAO ticketRepository,
                          EmailService emailService,
                          FlightRepositoryDAO flightRepository,
                          AirlineRepositoryDAO airlineRepository,
                          AirportRepositoryDAO airportRepository,
                          UserRepositoryDAO userRepository, TicketService ticketService) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.ticketRepository = ticketRepository;
        this.emailService = emailService;
        this.flightRepository = flightRepository;
        this.airlineRepository = airlineRepository;
        this.airportRepository = airportRepository;
        this.userRepository = userRepository;
        this.ticketService = ticketService;
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

        // 5) Generate tickets for every passenger
        for (Passenger passenger : passengers) {
            CreateTicketRequest ticket = new CreateTicketRequest();

            // Set the required fields from your existing data
            ticket.setBookingId(payment.getBookingId());
            ticket.setPassengerId(passenger.getId());
            ticket.setFlightId(booking.getFlightId());
            ticket.setSeatClass(booking.getSeatClass());

            // Set fare information (using your placeholder values)
            ticket.setBaseFare(BigDecimal.valueOf(3000)); // placeholder
            ticket.setTaxes(BigDecimal.valueOf(300));     // placeholder

            // Create the ticket using your service method
            TicketDTO createdTicket = ticketService.createTicket(ticket);
        }

        /*
        // OLD ONE
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
            byte[] pdf = generateTicketPdf(saved);

            // 2. Fetch passenger email
            String email = p.getEmail(); // make sure passenger model has email

            // 3. Send PDF via email
            try {
                emailService.sendTicketEmail(email, p.getFirstName(), pdf);
            } catch (MessagingException e) {
                throw new RuntimeException("Failed to send ticket email", e);
            }
        }
        */

        // 6) Generate a single PDF for the entire booking
        byte[] pdf = generateBookingItineraryPdf(booking.getId());

        // 7) Fetch the booking owner's email (primary contact)
        User bookingUser = userRepository.findById(booking.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found for booking"));
        String ownerEmail = bookingUser.getEmail();
        String ownerName = bookingUser.getFirstName();

        // 8) Send the booking itinerary PDF via email
        try {
            emailService.sendTicketEmail(ownerEmail, ownerName, pdf);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send booking itinerary email", e);
        }

        // 9) Update booking status → CONFIRMED
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        bookingRepository.update(booking);
    }

    public byte[] generateBookingItineraryPdf(Long bookingId) {
        // 1. Fetch the main entities with proper Optional handling
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found with id: " + bookingId));

        List<Passenger> passengers = passengerRepository.findByBookingId(bookingId);
        List<Ticket> tickets = ticketRepository.findByBookingId(bookingId);

        if (tickets.isEmpty()) {
            throw new TicketNotFoundException("No tickets found for booking id: " + bookingId);
        }

        // Get flight from booking's flightId
        Flight flight = flightRepository.findById(booking.getFlightId())
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with id: " + booking.getFlightId()));

        // 2. Resolve the related entities
        Airline airline = airlineRepository.findById(flight.getAirlineId())
                .orElseThrow(() -> new AirlineNotFoundException("Airline not found with id: " + flight.getAirlineId()));
        Airport departureAirport = airportRepository.findById(flight.getDepartureAirportId())
                .orElseThrow(() -> new AirportNotFoundException("Departure airport not found with id: " + flight.getDepartureAirportId()));
        Airport arrivalAirport = airportRepository.findById(flight.getArrivalAirportId())
                .orElseThrow(() -> new AirportNotFoundException("Arrival airport not found with id: " + flight.getArrivalAirportId()));

        // 3. Generate PDF - pass all tickets
        return generateTicketPdf(tickets, flight, booking, passengers, airline, departureAirport, arrivalAirport);
    }
    
}
