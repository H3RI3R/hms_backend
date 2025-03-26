package com.example.main.Report.Service;

import com.example.main.Exception.ResponseClass;
import com.example.main.Report.Entity.BookingPayments;
import com.example.main.Report.Repo.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReturnedPaymentsService {
    @Autowired
    private PaymentRepository bookingPaymentsRepo;
    public ResponseEntity<?> addReturnedPayment(String bookingNo, String userName, String userEmail, Double totalPaid,
                                                Double totalAmount, Double pendingAmount, String issuedBy) {
        BookingPayments payment = new BookingPayments();
        payment.setBookingNo(bookingNo);
        payment.setUserName(userName);
        payment.setUserEmail(userEmail);
        payment.setTotalPaid(totalPaid);
        payment.setTotalAmount(totalAmount);
        payment.setPendingAmount(pendingAmount);
        payment.setIssuedBy(issuedBy);
        payment.setDate(LocalDateTime.now());
        payment.setPaymentType("paymentReturned");

        bookingPaymentsRepo.save(payment);
        return ResponseClass.responseSuccess("Returned payment added successfully", "payment", payment);
    }
    public ResponseEntity<?> getAllReturnedPayments(String email) {
        List<BookingPayments> returnedPayments;

        if (email != null && !email.trim().isEmpty()) {
            returnedPayments = bookingPaymentsRepo.findByPaymentTypeAndUserEmail("paymentReturned", email);
            if (returnedPayments.isEmpty()) {
                return ResponseClass.responseFailure("No returned payments found for user " + email);
            }
            return ResponseClass.responseSuccess("Returned payments for user retrieved successfully", "returnedPayments", returnedPayments);
        }

        returnedPayments = bookingPaymentsRepo.findByPaymentType("paymentReturned");

        if (returnedPayments.isEmpty()) {
            return ResponseClass.responseFailure("No returned payments found");
        }
        return ResponseClass.responseSuccess("Returned payments retrieved successfully", "returnedPayments", returnedPayments);
    }

}
