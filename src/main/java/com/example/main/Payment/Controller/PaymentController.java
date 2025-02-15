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

//    @PostMapping("/pay/{bookingId}")
//    public ResponseEntity<?> offlinePayment(@PathVariable long bookingId,@RequestParam double amount){
//        return paymentService.offlinePayment(bookingId,amount);
//    }
//
//    @GetMapping("/getBookingDetails/{bookingId}")
//    public ResponseEntity<?> getBookingDetails(@RequestHeader("Authorization") String token,@PathVariable long bookingId) {
//        return paymentService.getBookingDetails(token,bookingId);
//    }
    @GetMapping("/getPaymentDetailsByPaymentId/{paymentId}")
    public ResponseEntity<?> getPaymentDetailsByPaymentId(@RequestHeader("Authorization") String token ,
                                                          @PathVariable Long paymentId){
        return paymentService.getPaymentDetailsByPaymentId(token, paymentId);
    }

    @GetMapping("/getAllPayments")
    public ResponseEntity<Map<String, Object>> getAllPayments(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "status", required = false) PaymentStatus status) {
        return paymentService.getPayments(token, status);
    }


    @PutMapping("/{status}/{paymentId}")
    public ResponseEntity<Map<String, Object>> statusPayment(
            @RequestHeader("Authorization") String token,
            @PathVariable PaymentStatus status,
            @PathVariable Long paymentId,
            @RequestParam(value = "refundAmount", required = false) Double refundAmount ){
        return paymentService.updatePaymentStatus(token, paymentId, status, refundAmount);
    }

}
