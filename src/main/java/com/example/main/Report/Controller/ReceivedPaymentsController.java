package com.example.main.Report.Controller;

import com.example.main.Report.Service.ReceivedPaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/payments/received")
public class ReceivedPaymentsController {
    @Autowired
    private ReceivedPaymentsService receivedPaymentsService;

    @GetMapping("/getAllPayments")
    public ResponseEntity<?> getAllPayments() {
        return receivedPaymentsService.getAllPayments();
    }
}
