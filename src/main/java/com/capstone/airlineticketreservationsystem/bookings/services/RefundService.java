package com.capstone.airlineticketreservationsystem.bookings.services;

import com.capstone.airlineticketreservationsystem.bookings.dtos.CreateRefundRequest;
import com.capstone.airlineticketreservationsystem.bookings.dtos.RefundDTO;
import com.capstone.airlineticketreservationsystem.bookings.exceptions.RefundNotFoundException;
import com.capstone.airlineticketreservationsystem.bookings.models.Refund;
import com.capstone.airlineticketreservationsystem.bookings.models.Payment;
import com.capstone.airlineticketreservationsystem.bookings.repositories.BookingRepositoryDAO;
import com.capstone.airlineticketreservationsystem.bookings.repositories.PaymentRepositoryDAO;
import com.capstone.airlineticketreservationsystem.bookings.repositories.RefundRepositoryDAO;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RefundService {

    private final RefundRepositoryDAO refundRepository;
    private final BookingRepositoryDAO bookingRepository;
    private final PaymentRepositoryDAO paymentRepository;

    public RefundService(
            RefundRepositoryDAO refundRepository,
            BookingRepositoryDAO bookingRepository,
            PaymentRepositoryDAO paymentRepository
    ) {
        this.refundRepository = refundRepository;
        this.bookingRepository = bookingRepository;
        this.paymentRepository = paymentRepository;
    }

    // ===================== CREATE REFUND =====================
    public RefundDTO createRefund(CreateRefundRequest req) {

        // 1. Validate Booking Exists (from bookingUUID)
        Long bookingId = bookingRepository.findByUUID(req.getBookingUUID())
                .orElseThrow(() -> new RefundNotFoundException("Booking not found"))
                .getId();

        // 2. Validate Payment Exists
        Payment payment = paymentRepository.findById(req.getPaymentId())
                .orElseThrow(() -> new RefundNotFoundException("Payment not found"));

        // 3. Validate payment belongs to the same booking
        if (!payment.getBookingId().equals(bookingId)) {
            throw new RefundNotFoundException("Payment does not belong to this booking");
        }

        // 4. Create refund
        Refund r = new Refund();
        r.setPaymentId(req.getPaymentId());
        r.setBookingId(bookingId);
        r.setRefundAmount(req.getRefundAmount());
        r.setCancellationFee(req.getCancellationFee());
        r.setRefundableAmount(req.getRefundAmount().subtract(req.getCancellationFee()));
        r.setRefundReason(req.getRefundReason());
        r.setRefundStatus(Refund.RefundStatus.PENDING);

        Refund saved = refundRepository.save(r);

        return convertToDTO(saved);
    }

    // ===================== GET REFUND BY UUID =====================
    public RefundDTO getRefundByUUID(String refundUUID) {
        Refund r = refundRepository.findByUUID(refundUUID)
                .orElseThrow(() -> new RefundNotFoundException("Refund not found"));

        return convertToDTO(r);
    }

    // ===================== GET REFUNDS FOR BOOKING =====================
    public List<RefundDTO> getRefundsByBooking(Long bookingId) {
        return refundRepository.findByBookingId(bookingId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // ===================== DELETE REFUND =====================
    public void deleteRefund(String refundUUID) {
        refundRepository.softDelete(refundUUID);
    }

    // ===================== CONVERTOR =====================
    private RefundDTO convertToDTO(Refund r) {
        return new RefundDTO(
                r.getRefundsUUID(),
                r.getPaymentId(),
                r.getBookingId(),
                r.getRefundAmount(),
                r.getCancellationFee(),
                r.getRefundableAmount(),
                r.getRefundReason(),
                r.getRefundStatus().name(),
                r.getRefundTransactionId(),
                r.getProcessedAt(),
                r.getCreatedAt()
        );
    }
}
