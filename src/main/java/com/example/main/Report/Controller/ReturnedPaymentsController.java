package com.example.main.Report.Controller;

import com.example.main.Report.Service.ReturnedPaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records/payments/returned")
public class ReturnedPaymentsController {
    @Autowired
    private ReturnedPaymentsService returnedPaymentsService;
    @GetMapping("/getAllPayments")
    public ResponseEntity<?> getAllReturnedPayments(@RequestHeader("Authorization") String token,
                                                    @RequestParam(value = "search" ,required = false)String email) {
        return returnedPaymentsService.getAllReturnedPayments(email);
    }
}
