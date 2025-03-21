package com.example.main.Payment.service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.Hotel.Entity.Booking;
import com.example.main.Hotel.Repo.BookingRepo;
import com.example.main.Payment.Repo.PaymentRepo;
import com.example.main.Payment.entity.PaymentClass;
import com.example.main.Payment.entity.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PaymentService {

    private final ConfigClass configClass;
    private final BookingRepo bookingRepo;

    private final PaymentRepo paymentRepo;
//    public ResponseEntity<?> offlinePayment(long bookingId, double amount) {
//        Booking booking = bookingRepo.findByBookingId(bookingId);
//        if(booking == null) {
//            return ResponseClass.responseSuccess("Booking not found with this booking no ");
//        }
//        PaymentClass paymentClass = paymentRepo.findByBookingNo(booking.getBookingNo());
//        if(paymentClass ==null)
//        {
//            return ResponseClass.responseFailure("Payment not found with this booking no");
//        }
//
//        booking.setTotalPaid(amount);
//        booking.setPendingAmount(booking.getTotalAmount()-booking.getTotalPaid());
//        if(booking.getPendingAmount()==0){
//            paymentClass.setPaymentStatus(PaymentStatus.SUCCESSFUL);
//        }
//        paymentClass.setPaymentType("Cash");
//        paymentRepo.save(paymentClass);
//        return ResponseClass.responseSuccess("Payment Successful");
//    }
//
//    public ResponseEntity<?> getBookingDetails(String token, long bookingId) {
//        Booking booking = bookingRepo.findByBookingId(bookingId);
//        if (booking == null) {
//            return ResponseClass.responseFailure("Booking not found");
//        }
//        PaymentClass paymentClass = paymentRepo.findByBookingNo(booking.getBookingNo());
//        if(paymentClass ==null)
//        {
//            return ResponseClass.responseFailure("Payment not found with this booking");
//        }
//        // Guest Info Data
//        List<Map<String, String>> guestInfoData = new ArrayList<>();
//        guestInfoData.add(Map.of("key", "Name", "value", booking.getGuestName()));
//        guestInfoData.add(Map.of("key", "Email", "value", booking.getGuestEmail()));
//        guestInfoData.add(Map.of("key", "Phone", "value", booking.getPhoneNo()));
//        guestInfoData.add(Map.of("key", "Address", "value", booking.getAddress()));
//
//        // Payment Summary Data
//        List<Map<String, Object>> paymentSummaryData = new ArrayList<>();
//        paymentSummaryData.add(Map.of("key", "Total Payment", "value", paymentClass.getTotalAmount()));
//        paymentSummaryData.add(Map.of("key", "Payment Received", "value", paymentClass.getTotalPaid()));
//        paymentSummaryData.add(Map.of("key", "Refunded", "value", paymentClass.getRefund()));
//        paymentSummaryData.add(Map.of("key", "Receivable from User", "value", paymentClass.getPendingAmount()));
//
//        // Payment Info Data
//        List<Map<String, Object>> paymentInfoData = new ArrayList<>();
//        paymentInfoData.add(Map.of("key", "Total Fare", "value", paymentClass.getTotalAmount() - paymentClass.getTax()));
//        paymentInfoData.add(Map.of("key", "Tax Charge", "value", paymentClass.getTax()));
//        paymentInfoData.add(Map.of("key", "Canceled Fare", "value", paymentClass.getCancelTax())); // Assuming static value
//        paymentInfoData.add(Map.of("key", "Canceled Tax Charge", "value", paymentClass.getCancelTaxCharge()));
//        paymentInfoData.add(Map.of("key", "Extra Service Charge", "value", paymentClass.getExtraService()));
//        paymentInfoData.add(Map.of("key", "Other Charges", "value", paymentClass.getExtraService())); // Assuming static value
//        paymentInfoData.add(Map.of("key", "Cancellation Fee", "value", paymentClass.getCancelTax()));
//        paymentInfoData.add(Map.of("key", "Total Amount", "value", paymentClass.getTotalAmount()));
//
//        // Aggregating all data
//        Map<String, Object> responseData = new HashMap<>();
//        responseData.put("guestInfoData", guestInfoData);
//        responseData.put("paymentSummaryData", paymentSummaryData);
//        responseData.put("paymentInfoData", paymentInfoData);
//
//        return ResponseClass.responseSuccess("Booking details fetched successfully", "bookingDetails", responseData);
//    }


    // Update status Accept and Reject
    // Update status Accept and Reject
    public ResponseEntity<Map<String, Object>> updatePaymentStatus(String token, Long paymentId, PaymentStatus newStatus, Double refundAmount) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        Optional<PaymentClass> paymentOptional = paymentRepo.findById(paymentId);

        if (paymentOptional.isEmpty()) {
            return ResponseClass.responseFailure("Payment not found with ID: " + paymentId);
        }

        PaymentClass payment = paymentOptional.get();

        if (refundAmount == null) {
            refundAmount = 0.0;
        }

        if (refundAmount > payment.getTotalPaid()) {
            return ResponseClass.responseFailure("Refund amount cannot be greater than paid amount.");
        }

        if (!payment.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Unauthorized: Payment does not belong to your hotel.");
        }

        if (newStatus == PaymentStatus.REJECTED) {
            payment.setPaymentStatus(PaymentStatus.REJECTED);
            payment.setRefundAmount(refundAmount);
            payment.setPendingAmount(0.0);

            double recievedAmount = payment.getTotalPaid() - refundAmount;
            payment.setRecievedAmount(recievedAmount);

        } else if (newStatus == PaymentStatus.ACCEPTED) {
            payment.setPaymentStatus(PaymentStatus.ACCEPTED);
            payment.setPaymentStatus(PaymentStatus.SUCCESSFUL);
            payment.setPendingAmount(0);
            payment.setTotalPaid(payment.getTotalAmount());
        } else {
            return ResponseClass.responseFailure("Status Not Updated");
        }

        paymentRepo.save(payment);
        return ResponseClass.responseSuccess("Payment status updated successfully", "updatedPayment", payment);
    }

    public ResponseEntity<?> getPaymentDetailsByPaymentId(String token, Long paymentId) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        Optional<PaymentClass> paymentOptional = paymentRepo.findById(paymentId);
        if (paymentOptional.isEmpty()) {
            return ResponseClass.responseFailure("Payment not found with ID: " + paymentId);
        }
        PaymentClass payment = paymentOptional.get();
        // Ensure the payment belongs to the hotel making the request
        if (!payment.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Unauthorized: Payment does not belong to your hotel.");
        }
        return ResponseClass.responseSuccess("User found successfully", "UserPayment", payment);
    }

//    //Get all payments
//    public ResponseEntity<Map<String, Object>> getPaymentsByHotelId(String token) {
//        String hotelId = configClass.tokenValue(token, "hotelId");
//        List<PaymentClass> payments = paymentRepo.findByHotelId(hotelId);
//
//        if (payments.isEmpty()) {
//            return ResponseClass.responseFailure("No payments found for this hotel.");
//        }
//        return ResponseClass.responseSuccess("Payments retrieved successfully", "payments", payments);
//    }

    //Get payments by status
    public ResponseEntity<Map<String, Object>> getPayments(String token, PaymentStatus status, String search, String fromDate, String toDate, int page, int size ,  Long bookingId) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<PaymentClass> paymentsPage;

        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        if (fromDate != null && !fromDate.isEmpty()) {
            LocalDate parsedFromDate = LocalDate.parse(fromDate, formatter);
            fromDateTime = parsedFromDate.atStartOfDay();
        }
        if (toDate != null && !toDate.isEmpty()) {
            LocalDate parsedToDate = LocalDate.parse(toDate, formatter);
            toDateTime = parsedToDate.atTime(23, 59, 59);
        }

        if (bookingId != null) {
            paymentsPage = paymentRepo.findByHotelIdAndBookingId(hotelId, bookingId, pageable);
        } else if  (search != null && !search.isEmpty()) {
            boolean isEmail = search.contains("@");
            paymentsPage = isEmail
                    ? paymentRepo.findByHotelIdAndUserEmailContaining(hotelId, search, pageable)
                    : paymentRepo.findByHotelIdAndUserNameContaining(hotelId, search, pageable);

            if (paymentsPage.isEmpty()) {
                String message = isEmail ? "No account found with email: " + search : "No account found with username: " + search;
                return ResponseClass.responseFailure(message);
            }
            if (status != null) {
                List<PaymentClass> filteredPayments = paymentsPage.getContent().stream()
                        .filter(payment -> payment.getPaymentStatus() == status)
                        .collect(Collectors.toList());

                if (filteredPayments.isEmpty()) {
                    return ResponseClass.responseFailure("No payments found with the given search and status.");
                }

                paymentsPage = new PageImpl<>(filteredPayments, pageable, filteredPayments.size());
            }
        }
        else if (status != null && fromDateTime != null && toDateTime != null) {
            paymentsPage = paymentRepo.findByHotelIdAndPaymentStatusAndPaymentDateBetween(hotelId, status, fromDateTime, toDateTime, pageable);
        }
        else if (fromDateTime != null && toDateTime != null) {
            paymentsPage = paymentRepo.findByHotelIdAndPaymentDateBetween(hotelId, fromDateTime, toDateTime, pageable);
        }
        else if (status != null) {
            paymentsPage = paymentRepo.findByHotelIdAndPaymentStatus(hotelId, status, pageable);
        }
        else {
            paymentsPage = paymentRepo.findByHotelId(hotelId, pageable);
        }

        if (paymentsPage.isEmpty()) {
            return ResponseClass.responseFailure("No payments found for this hotel.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Payments retrieved successfully");
        response.put("payments", paymentsPage.getContent());
        response.put("currentPage", paymentsPage.getNumber() + 1);
        response.put("totalPages", paymentsPage.getTotalPages());
        response.put("totalItems", paymentsPage.getTotalElements());

        return ResponseEntity.ok(response);
    }
}