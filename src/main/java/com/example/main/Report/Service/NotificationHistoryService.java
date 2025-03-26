package com.example.main.Report.Service;
import com.example.main.Exception.ResponseClass;
import com.example.main.Report.Entity.NotificationHistory;
import com.example.main.Report.Repo.NotificationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationHistoryService {


    @Autowired
    private NotificationHistoryRepository notificationHistoryRepository;
    public ResponseEntity<Map<String, Object>> getAllNotifications(String searchKey) {
        if (searchKey == null || searchKey.trim().isEmpty()) {
            List<NotificationHistory> notifications = notificationHistoryRepository.findAll();
            if (notifications.isEmpty()) {
                return ResponseClass.responseFailure("No notifications found");
            }
            return ResponseClass.responseSuccess("Notifications fetched successfully", "notifications", notifications);
        }

        if (isValidEmail(searchKey)) {
            List<NotificationHistory> notifications = notificationHistoryRepository.findByUserEmail(searchKey);
            if (notifications.isEmpty()) {
                return ResponseClass.responseFailure("No notifications found for this user: " + searchKey);
            }
            return ResponseClass.responseSuccess("Notifications for user fetched successfully", "notifications", notifications);
        } else {
            return ResponseClass.responseFailure("Invalid email format: " + searchKey);
        }
    }

    private boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public ResponseEntity<?> getMessageOnlyById(Long id) {
        Optional<NotificationHistory> notification = notificationHistoryRepository.findById(id);

        if (notification.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("message", notification.get().getMessage());
            return ResponseEntity.ok(response);
        } else {
            return ResponseClass.responseFailure("Notification not found with ID: " + id);
        }
    }
}