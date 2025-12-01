package com.capstone.airlineticketreservationsystem.utilities;

import com.capstone.airlineticketreservationsystem.bookings.models.Booking;
import com.capstone.airlineticketreservationsystem.bookings.models.Passenger;
import com.capstone.airlineticketreservationsystem.bookings.models.Ticket;
import com.capstone.airlineticketreservationsystem.flights.models.Airline;
import com.capstone.airlineticketreservationsystem.flights.models.Airport;
import com.capstone.airlineticketreservationsystem.flights.models.Flight;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.util.Locale;

public class TicketPdfGenerator {

    public static byte[] generateTicketPdf(Ticket ticket) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);

            PdfWriter.getInstance(document, out);
            document.open();

            // -------------------------------
            // TITLE SECTION
            // -------------------------------
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("AIRLINE E-TICKET", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // -------------------------------
            // PASSENGER + BOOKING INFO TABLE
            // -------------------------------
            PdfPTable table = new PdfPTable(2);
            table.setWidthPercentage(100);

            addCell(table, "Ticket Number:", ticket.getTicketNumber());
            addCell(table, "Booking ID:", String.valueOf(ticket.getBookingId()));
            addCell(table, "Passenger ID:", String.valueOf(ticket.getPassengerId()));
            addCell(table, "Flight ID:", String.valueOf(ticket.getFlightId()));
            addCell(table, "Seat Class:", ticket.getSeatClass().name());
            addCell(table, "Base Fare:", ticket.getBaseFare().toString());
            addCell(table, "Taxes:", ticket.getTaxes().toString());
            addCell(table, "Total Fare:", ticket.getTotalFare().toString());
            addCell(table, "Ticket Status:", ticket.getTicketStatus().name());

            document.add(table);

            // -------------------------------
            // FOOTER
            // -------------------------------
            Paragraph footer = new Paragraph(
                    "\nThank you for choosing our Airline!",
                    FontFactory.getFont(FontFactory.HELVETICA, 12)
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    public static byte[] generateTicketPdf(List<Ticket> tickets, Flight flight, Booking booking,
                                           List<Passenger> passengers, Airline airline,
                                           Airport departureAirport, Airport arrivalAirport) {

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, out);
            document.open();

            // -------------------------------
            // TITLE SECTION
            // -------------------------------
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA, 20, Font.BOLD);
            Paragraph title = new Paragraph("E-TICKET RECEIPT", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingAfter(20f);
            document.add(title);

            // -------------------------------
            // FLIGHT INFORMATION
            // -------------------------------
            Font sectionFont = FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLD);
            Paragraph flightSection = new Paragraph("Flight Information", sectionFont);
            flightSection.setSpacingAfter(10f);
            document.add(flightSection);

            PdfPTable flightTable = new PdfPTable(2);
            flightTable.setWidthPercentage(100);

            addCell(flightTable, "Airline:", airline.getAirlineName() + " (" + airline.getAirlineCode() + ")");
            addCell(flightTable, "Flight Number:", flight.getFlightNumber());
            addCell(flightTable, "Route:", departureAirport.getAirportCode() + " - " + arrivalAirport.getAirportCode());
            addCell(flightTable, "Departure:", formatDateTime(flight.getScheduledDeparture()) + "\n" +
                    departureAirport.getAirportName() + ", " + departureAirport.getCity());
            addCell(flightTable, "Arrival:", formatDateTime(flight.getScheduledArrival()) + "\n" +
                    arrivalAirport.getAirportName() + ", " + arrivalAirport.getCity());
            addCell(flightTable, "Duration:", calculateDuration(flight.getScheduledDeparture(), flight.getScheduledArrival()));

            document.add(flightTable);

            // -------------------------------
            // BOOKING INFORMATION
            // -------------------------------
            Paragraph bookingSection = new Paragraph("Booking Information", sectionFont);
            bookingSection.setSpacingBefore(15f);
            bookingSection.setSpacingAfter(10f);
            document.add(bookingSection);

            PdfPTable bookingTable = new PdfPTable(2);
            bookingTable.setWidthPercentage(100);

            addCell(bookingTable, "Booking Reference (PNR):", booking.getPnr());
            addCell(bookingTable, "Booking Status:", booking.getBookingStatus().name());
            addCell(bookingTable, "Total Passengers:", String.valueOf(passengers.size()));

            document.add(bookingTable);

            // -------------------------------
            // PASSENGER & TICKET DETAILS TABLE
            // -------------------------------
            Paragraph passengerSection = new Paragraph("Passenger & Ticket Details", sectionFont);
            passengerSection.setSpacingBefore(15f);
            passengerSection.setSpacingAfter(10f);
            document.add(passengerSection);

            // Create table for passenger list
            PdfPTable passengerTable = new PdfPTable(4);
            passengerTable.setWidthPercentage(100);
            passengerTable.setWidths(new float[]{3, 2, 1, 1});

            // Add table headers
            String[] headers = {"Passenger Name", "Ticket Number", "Type", "Class"};
            for (String header : headers) {
                PdfPCell headerCell = new PdfPCell(new Phrase(header, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD)));
                headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                headerCell.setBackgroundColor(new BaseColor(220, 220, 220)); // Fixed BaseColor
                headerCell.setPadding(5);
                passengerTable.addCell(headerCell);
            }

            // Add passenger rows - match each passenger with their ticket
            for (int idx = 0; idx < passengers.size(); idx++) {
                Passenger passenger = passengers.get(idx);
                Ticket ticket = tickets.get(idx); // Get corresponding ticket

                passengerTable.addCell(new Phrase(passenger.getFirstName() + " " + passenger.getLastName()));
                passengerTable.addCell(new Phrase(ticket.getTicketNumber()));
                passengerTable.addCell(new Phrase(passenger.getPassengerType().name()));
                passengerTable.addCell(new Phrase(ticket.getSeatClass().name()));
            }

            document.add(passengerTable);

            // -------------------------------
            // FARE SUMMARY FOR ENTIRE BOOKING
            // -------------------------------
            Paragraph fareSection = new Paragraph("Fare Summary", sectionFont);
            fareSection.setSpacingBefore(15f);
            fareSection.setSpacingAfter(10f);
            document.add(fareSection);

            // Calculate totals across all tickets
            BigDecimal totalBaseFare = BigDecimal.ZERO;
            BigDecimal totalTaxes = BigDecimal.ZERO;
            BigDecimal totalAncillary = BigDecimal.ZERO;
            BigDecimal grandTotal = BigDecimal.ZERO;

            for (Ticket ticket : tickets) {
                totalBaseFare = totalBaseFare.add(ticket.getBaseFare());
                totalTaxes = totalTaxes.add(ticket.getTaxes());
                totalAncillary = totalAncillary.add(ticket.getAncillaryCharges());
                grandTotal = grandTotal.add(ticket.getTotalFare());
            }

            PdfPTable fareTable = new PdfPTable(2);
            fareTable.setWidthPercentage(50);
            fareTable.setHorizontalAlignment(Element.ALIGN_RIGHT);

            // Use simple currency formatting if FormattingUtils isn't available
            addCell(fareTable, "Base Fare:", formatCurrency(totalBaseFare));
            addCell(fareTable, "Taxes & Fees:", formatCurrency(totalTaxes));
            if (totalAncillary.compareTo(BigDecimal.ZERO) > 0) {
                addCell(fareTable, "Ancillary Charges:", formatCurrency(totalAncillary));
            }
            addCell(fareTable, "Total Fare:", formatCurrency(grandTotal));

            document.add(fareTable);

            // -------------------------------
            // FOOTER
            // -------------------------------
            Paragraph footer = new Paragraph(
                    "\nPlease present this e-ticket and valid identification at check-in.\n" +
                            "Check-in opens 24 hours before departure and closes 45 minutes prior to departure.\n" +
                            "Thank you for choosing " + airline.getAirlineName() + "!",
                    FontFactory.getFont(FontFactory.HELVETICA, 10)
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

    // Keep your existing helper method
    private static void addCell(PdfPTable table, String key, String value) {
        PdfPCell keyCell = new PdfPCell(new Phrase(key));
        keyCell.setPadding(8);
        keyCell.setBorderWidth(1);

        PdfPCell valueCell = new PdfPCell(new Phrase(value));
        valueCell.setPadding(8);
        valueCell.setBorderWidth(1);

        table.addCell(keyCell);
        table.addCell(valueCell);
    }

    // Simple formatting helpers
    private static String formatDateTime(LocalDateTime dateTime) {
        return dateTime.format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy 'at' HH:mm"));
    }

    private static String formatCurrency(BigDecimal amount) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "IN"));
        return currencyFormat.format(amount);
    }

    private static String calculateDuration(LocalDateTime departure, LocalDateTime arrival) {
        java.time.Duration duration = java.time.Duration.between(departure, arrival);
        long hours = duration.toHours();
        long minutes = duration.toMinutesPart();
        return String.format("%dh %02dm", hours, minutes);
    }
}
