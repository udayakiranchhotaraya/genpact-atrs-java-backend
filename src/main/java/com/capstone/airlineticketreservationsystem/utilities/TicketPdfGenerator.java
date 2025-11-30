package com.capstone.airlineticketreservationsystem.utilities;

import com.capstone.airlineticketreservationsystem.bookings.models.Ticket;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

import java.io.ByteArrayOutputStream;

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
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
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
                    new Font(Font.HELVETICA, 12)
            );
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("PDF generation failed", e);
        }
    }

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
}
