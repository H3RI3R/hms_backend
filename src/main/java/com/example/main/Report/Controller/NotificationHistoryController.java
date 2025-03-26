package com.example.main.Report.Controller;

import com.example.main.Report.Entity.NotificationHistory;
import com.example.main.Report.Service.NotificationHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/report/notification/history")
public class NotificationHistoryController {

    @Autowired
    private NotificationHistoryService notificationHistoryService;


    @GetMapping("/getAll")
    public ResponseEntity<Map<String, Object>> getAllNotifications(@RequestHeader("Authorization") String token,
                                                                    @RequestParam (value = "search" ,required = false)String searchKey
    ) {
        return notificationHistoryService.getAllNotifications( searchKey);
    }
    @GetMapping("/getMessageById/{id}")
    public ResponseEntity<?> getMessageById(@RequestHeader("Authorization") String token,@PathVariable Long id) {
        return notificationHistoryService.getMessageOnlyById(id);
    }
}