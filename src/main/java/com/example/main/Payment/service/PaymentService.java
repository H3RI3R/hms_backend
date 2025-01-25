package com.example.main.Payment.service;

import com.example.main.Exception.ResponseClass;
import com.example.main.Hotel.Entity.Booking;
import com.example.main.Hotel.Repo.BookingRepo;
import com.example.main.Payment.Repo.PaymentRepo;
import com.example.main.Payment.entity.PaymentClass;
import com.example.main.Payment.entity.PaymentStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PaymentService {


    private final BookingRepo bookingRepo;

    private final PaymentRepo paymentRepo;
    public ResponseEntity<?> offlinePayment(long bookingId, double amount) {
        Booking booking = bookingRepo.findByBookingId(bookingId);
        if(booking == null) {
            return ResponseClass.responseSuccess("Booking not found with this booking no ");
        }
        PaymentClass paymentClass = paymentRepo.findByBookingNo(booking.getBookingNo());
        if(paymentClass ==null)
        {
            return ResponseClass.responseFailure("Payment not found with this booking no");
        }

        booking.setTotalPaid(amount);
        booking.setPendingAmount(booking.getTotalAmount()-booking.getTotalPaid());
        if(booking.getPendingAmount()==0){
            paymentClass.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        }
        paymentClass.setPaymentType("Cash");
        paymentRepo.save(paymentClass);
        return ResponseClass.responseSuccess("Payment Successful");
    }

    public ResponseEntity<?> getBookingDetails(String token, long bookingId) {
        Booking booking = bookingRepo.findByBookingId(bookingId);
        if (booking == null) {
            return ResponseClass.responseFailure("Booking not found");
        }
        PaymentClass paymentClass = paymentRepo.findByBookingNo(booking.getBookingNo());
        if(paymentClass ==null)
        {
            return ResponseClass.responseFailure("Payment not found with this booking");
        }
        // Guest Info Data
        List<Map<String, String>> guestInfoData = new ArrayList<>();
        guestInfoData.add(Map.of("key", "Name", "value", booking.getGuestName()));
        guestInfoData.add(Map.of("key", "Email", "value", booking.getGuestEmail()));
        guestInfoData.add(Map.of("key", "Phone", "value", booking.getPhoneNo()));
        guestInfoData.add(Map.of("key", "Address", "value", booking.getAddress()));

        // Payment Summary Data
        List<Map<String, Object>> paymentSummaryData = new ArrayList<>();
        paymentSummaryData.add(Map.of("key", "Total Payment", "value", paymentClass.getTotalAmount()));
        paymentSummaryData.add(Map.of("key", "Payment Received", "value", paymentClass.getTotalPaid()));
        paymentSummaryData.add(Map.of("key", "Refunded", "value", paymentClass.getRefund()));
        paymentSummaryData.add(Map.of("key", "Receivable from User", "value", paymentClass.getPendingAmount()));

        // Payment Info Data
        List<Map<String, Object>> paymentInfoData = new ArrayList<>();
        paymentInfoData.add(Map.of("key", "Total Fare", "value", paymentClass.getTotalAmount() - paymentClass.getTax()));
        paymentInfoData.add(Map.of("key", "Tax Charge", "value", paymentClass.getTax()));
        paymentInfoData.add(Map.of("key", "Canceled Fare", "value", paymentClass.getCancelTax())); // Assuming static value
        paymentInfoData.add(Map.of("key", "Canceled Tax Charge", "value", paymentClass.getCancelTaxCharge()));
        paymentInfoData.add(Map.of("key", "Extra Service Charge", "value", paymentClass.getExtraService()));
        paymentInfoData.add(Map.of("key", "Other Charges", "value", paymentClass.getExtraService())); // Assuming static value
        paymentInfoData.add(Map.of("key", "Cancellation Fee", "value", paymentClass.getCancelTax()));
        paymentInfoData.add(Map.of("key", "Total Amount", "value", paymentClass.getTotalAmount()));

        // Aggregating all data
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("guestInfoData", guestInfoData);
        responseData.put("paymentSummaryData", paymentSummaryData);
        responseData.put("paymentInfoData", paymentInfoData);

        return ResponseClass.responseSuccess("Booking details fetched successfully", "bookingDetails", responseData);
    }
}
