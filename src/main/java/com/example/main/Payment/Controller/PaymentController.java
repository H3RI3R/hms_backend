package com.example.main.Payment.Controller;

import com.example.main.Exception.ResponseClass;
import com.example.main.Payment.entity.PaymentClass;
import com.example.main.Payment.entity.PaymentStatus;
import com.example.main.Payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RequestMapping("/payment")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/pay/{bookingId}")
    public ResponseEntity<?> offlinePayment(@PathVariable long bookingId,@RequestParam double amount){
        return paymentService.offlinePayment(bookingId,amount);
    }

    @GetMapping("/getBookingDetails/{bookingId}")
    public ResponseEntity<?> getBookingDetails(@RequestHeader("Authorization") String token,@PathVariable long bookingId) {
        return paymentService.getBookingDetails(token,bookingId);
    }
    @GetMapping("/getPaymentDetailsByPaymentId/{paymentId}")
    public ResponseEntity<?> getPaymentDetailsByPaymentId(@RequestHeader("Authorization") String token ,
                                                          @PathVariable Long paymentId){
        return paymentService.getPaymentDetailsByPaymentId(token, paymentId);
    }
    // All  Payments
    @GetMapping("/getAllPaymentByHotelId")
    public ResponseEntity<Map<String, Object>> getPaymentsByHotel(@RequestHeader("Authorization") String token) {
        return paymentService.getPaymentsByHotelId(token);
    }

    @GetMapping("/getPendingPayments")
    public ResponseEntity<Map<String, Object>> getPendingPayments(@RequestHeader("Authorization") String token) {
        return paymentService.getPaymentsByStatus(token, PaymentStatus.PENDING, "Pending payments retrieved successfully");
    }

    @GetMapping("/getApprovedPayments")
    public ResponseEntity<Map<String, Object>> getApprovedPayments(@RequestHeader("Authorization") String token) {
        return paymentService.getPaymentsByStatus(token, PaymentStatus.ACCEPTED, "Approved payments retrieved successfully");
    }

    @GetMapping("/getSuccessfulPayments")
    public ResponseEntity<Map<String, Object>> getSuccessfulPayments(@RequestHeader("Authorization") String token) {
        return paymentService.getPaymentsByStatus(token, PaymentStatus.SUCCESSFUL, "Successful payments retrieved successfully");
    }

    @GetMapping("/getRejectedPayments")
    public ResponseEntity<Map<String, Object>> getRejectedPayments(@RequestHeader("Authorization") String token) {
        return paymentService.getPaymentsByStatus(token, PaymentStatus.REJECTED, "Rejected payments retrieved successfully");
    }

    @GetMapping("/getFailedPayments")
    public ResponseEntity<Map<String, Object>> getFailedPayments(@RequestHeader("Authorization") String token) {
        return paymentService.getPaymentsByStatus(token, PaymentStatus.FAILED, "Failed payments retrieved successfully");
    }

    //Accept
    @PutMapping("/acceptPayment/{paymentId}")
    public ResponseEntity<Map<String, Object>> acceptPayment(@RequestHeader("Authorization") String token,
                                                             @PathVariable Long paymentId) {
        return paymentService.updatePaymentStatus(token, paymentId, PaymentStatus.ACCEPTED);
    }
    //Reject
    @PutMapping("/rejectPayment/{paymentId}")
    public ResponseEntity<Map<String, Object>> rejectPayment(@RequestHeader("Authorization") String token,
                                                             @PathVariable Long paymentId) {
        return paymentService.updatePaymentStatus(token, paymentId, PaymentStatus.REJECTED);
    }

}
