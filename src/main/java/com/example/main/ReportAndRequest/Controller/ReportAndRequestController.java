package com.example.main.ReportAndRequest.Controller;

import com.example.main.ReportAndRequest.Service.ReportAndRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/request-report")
public class ReportAndRequestController {

@Autowired
    private  ReportAndRequestService reportAndRequestService;

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createReport(
            @RequestHeader("Authorization") String token,
            @RequestParam("type") String type,
            @RequestParam("message") String message) {
        return reportAndRequestService.createReport(token, type, message);
    }

    @GetMapping("/getAll")
    public ResponseEntity<Map<String, Object>> getAllReports() {
        return reportAndRequestService.getAllReports();
    }

}