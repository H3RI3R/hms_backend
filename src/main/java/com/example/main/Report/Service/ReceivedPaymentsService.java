package com.example.main.Report.Service;

import com.example.main.Exception.ResponseClass;
import com.example.main.Report.Entity.BookingPayments;
import com.example.main.Report.Entity.NotificationHistory;
import com.example.main.Report.Repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.print.Book;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReceivedPaymentsService {
    @Autowired
    private PaymentRepository paymentRepository;

    public ResponseEntity<?> processPayment(String paymentType, String bookingNo, String guestName,
                                            String email, Double totalPaid, String roleType ,double totalAmount , double pendingAmount) {

        if (totalPaid == null || totalPaid <= 0) {
            return ResponseClass.responseFailure("Invalid payment amount");
        }

        BookingPayments payment = new BookingPayments();
        payment.setPaymentType(paymentType); // "paymentReceived" or "paymentReturned"
        payment.setBookingNo(bookingNo);
        payment.setUserName(guestName);
        payment.setUserEmail(email);
        payment.setTotalPaid(totalPaid);
        payment.setTotalAmount(totalAmount);
        payment.setPendingAmount(pendingAmount);
        payment.setIssuedBy(roleType);
        payment.setDate(LocalDateTime.now());

        paymentRepository.save(payment);

        return ResponseClass.responseSuccess("Payment processed successfully", "payment", payment);
    }
    public ResponseEntity<?> getAllPayments(String searchkey, String startDateStr, String endDateStr, Integer page, Integer size) {

        if ((page != null && size == null) || (size != null && page == null)) {
            return ResponseClass.responseFailure("Both page and size must be provided together.");
        }

        List<BookingPayments> bookingPayments;
        Pageable pageable = null;
        Page<BookingPayments> pagedResult = null;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        try {
            if (startDateStr != null && !startDateStr.isEmpty()) {
                startDate = LocalDateTime.parse(startDateStr + " 00:00:00", formatter);
            }
            if (endDateStr != null && !endDateStr.isEmpty()) {
                endDate = LocalDateTime.parse(endDateStr + " 23:59:59", formatter);
            }
        } catch (Exception e) {
            return ResponseClass.responseFailure("Invalid date format, please use yyyy-MM-dd");
        }

        if (page != null && size != null) {
            pageable = PageRequest.of(page, size, Sort.by("date").descending());
        }

        if (searchkey != null && !searchkey.trim().isEmpty()) {
            if (searchkey.contains("@")) {
                if (pageable != null) {
                    pagedResult = paymentRepository.findByPaymentTypeAndUserEmail("paymentReceived", searchkey, pageable);
                    bookingPayments = pagedResult.getContent();
                } else {
                    bookingPayments = paymentRepository.findByPaymentTypeAndUserEmail("paymentReceived", searchkey);
                }
                if (bookingPayments.isEmpty()) {
                    return ResponseClass.responseFailure("No returned payment found for user " + searchkey);
                }
            } else {
                if (pageable != null) {
                    pagedResult = paymentRepository.findByPaymentTypeAndBookingNo("paymentReceived", searchkey, pageable);
                    bookingPayments = pagedResult.getContent();
                } else {
                    bookingPayments = paymentRepository.findByPaymentTypeAndBookingNo("paymentReceived", searchkey);
                }
                if (bookingPayments.isEmpty()) {
                    return ResponseClass.responseFailure("No payment found for booking number " + searchkey);
                }
            }
        } else {
            if (startDate != null && endDate != null) {
                if (pageable != null) {
                    pagedResult = paymentRepository.findByPaymentTypeAndDateBetween("paymentReceived", startDate, endDate, pageable);
                    bookingPayments = pagedResult.getContent();
                } else {
                    bookingPayments = paymentRepository.findByPaymentTypeAndDateBetween("paymentReceived", startDate, endDate);
                }
            } else {
                if (pageable != null) {
                    pagedResult = paymentRepository.findByPaymentType("paymentReceived", pageable);
                    bookingPayments = pagedResult.getContent();
                } else {
                    bookingPayments = paymentRepository.findByPaymentType("paymentReceived");
                }
            }
        }

        if (bookingPayments.isEmpty()) {
            return ResponseClass.responseFailure("No payments found");
        }

        if (pagedResult != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Payments retrieved successfully");
            response.put("payments", bookingPayments);
            response.put("totalElements", pagedResult.getTotalElements());
            response.put("totalPages", pagedResult.getTotalPages());
            response.put("currentPage", pagedResult.getNumber());
            response.put("size", pagedResult.getSize());
            return ResponseEntity.ok(response);
        }

        return ResponseClass.responseSuccess("Payments retrieved successfully", "payments", bookingPayments);
    }
}
