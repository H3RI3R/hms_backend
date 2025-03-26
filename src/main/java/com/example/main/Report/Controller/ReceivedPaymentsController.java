package com.example.main.Report.Controller;

import com.example.main.Report.Service.ReceivedPaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/records/payments/received")
public class ReceivedPaymentsController {
    @Autowired
    private ReceivedPaymentsService receivedPaymentsService;

    @GetMapping("/getAllPayments")
    public ResponseEntity<?> getAllPayments(@RequestHeader("Authorization") String token,
                                            @RequestParam(value = "search", required = false) String searchkey,
                                            @RequestParam(value = "fromDate", required = false) String startDate,
                                            @RequestParam(value = "toDate", required = false) String endDate,
                                            @RequestParam(value = "page" , required = false)Integer page,
                                            @RequestParam(value = "size" , required = false)Integer size ) {
        return receivedPaymentsService.getAllPayments(searchkey, startDate, endDate , page,size);
    }
}
