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
    public ResponseEntity<?> getAllPayments( String email) {
        if(email!=null){
            List<BookingPayments>  receivedPayments = paymentRepository.findByPaymentTypeAndUserEmail("paymentReceived",email);
            if (receivedPayments == null || receivedPayments.isEmpty()) {
                return ResponseClass.responseFailure("No returned payment found for user " + email);
            }
            return ResponseClass.responseSuccess("Payments retrieved for user successfully", "payments", receivedPayments);
        }
        List<BookingPayments> receivedPayments = paymentRepository.findByPaymentType("paymentReceived");
        if (receivedPayments.isEmpty()) {
            return ResponseClass.responseFailure("No payments found");
        }
        return ResponseClass.responseSuccess("Payments retrieved successfully", "payments", receivedPayments);
    }
}
