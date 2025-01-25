package com.example.main.Report.Controller;

import com.example.main.Exception.ResponseClass;
import com.example.main.Report.Entity.BookingAction;
import com.example.main.Report.Service.BookingActionsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report")
public class BookingActionsController {
    @Autowired
 private BookingActionsService bookingActionsService;
    @GetMapping("/booking/{bookingNo}")
    public ResponseEntity<?> getActionReportByBookingNo(@RequestHeader("Authorization") String token,
                                                        @PathVariable String bookingNo) {
        return bookingActionsService.getActionReportByBookingNo(bookingNo);
    }
    @GetMapping("booking/getAll")
    public ResponseEntity<Map<String, Object>> getAllBookingActions(@RequestHeader("Authorization") String token) {
        List<BookingAction> bookingActions = bookingActionsService.getAllBookingActions();

        return ResponseClass.responseSuccess("Booking actions retrieved successfully", "bookingReport", bookingActions);
    }


}
