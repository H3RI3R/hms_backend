package com.example.main.Report.Controller;

import com.example.main.Report.Service.ReturnedPaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/records/payments/returned")
public class ReturnedPaymentsController {
    @Autowired
    private ReturnedPaymentsService returnedPaymentsService;
    @GetMapping("/getAllPayments")
    public ResponseEntity<?> getAllReturnedPayments() {
        return returnedPaymentsService.getAllReturnedPayments();
    }
}
