package com.example.main.Dashboard.Controller;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Dashboard.Service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;
    private final ConfigClass configClass;

    @GetMapping("/metrics")
    public ResponseEntity<?> getDashboardMetrics(
            @RequestHeader("Authorization") String token) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return dashboardService.getDashboardMetrics(hotelId);
    }

    @GetMapping("/booking-report")
    public ResponseEntity<?> getBookingReport(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "fromDate", required = false) String startDate,
            @RequestParam(value = "toDate",required = false) String endDate) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return dashboardService.getBookingReport(hotelId, startDate, endDate);
    }

    @GetMapping("/payment-report")
    public ResponseEntity<?> getPaymentReport(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Integer startYear,
            @RequestParam(required = false) Integer endYear) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return dashboardService.getPaymentReport(hotelId, startYear, endYear);
    }
}