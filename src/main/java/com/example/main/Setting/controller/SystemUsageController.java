package com.example.main.Setting.controller;

import com.example.main.Configuration.SystemUsageMonitor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/monitor")
public class SystemUsageController {

    // Endpoint to check system usage
    @GetMapping("/usage")
    public String getSystemUsage() {
        // Call the SystemUsageMonitor to get and print system usage
        SystemUsageMonitor.printSystemUsage();

        // You can also return a response to the client if needed
        return "System health check completed, check logs for details.";
    }
}