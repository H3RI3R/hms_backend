package com.example.main.Payment.Controller;

import com.example.main.Payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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








}
