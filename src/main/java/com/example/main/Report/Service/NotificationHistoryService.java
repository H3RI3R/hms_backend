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

@Service
public class NotificationHistoryService {


    @Autowired
    private NotificationHistoryRepository notificationHistoryRepository;

    public ResponseEntity<Map<String, Object>> getAllNotifications(String email) {
        if(email!=null){
            List<NotificationHistory> notifications = notificationHistoryRepository.findByUserEmail(email);
            if(notifications.isEmpty()){
                return ResponseClass.responseFailure("No notification found for this user "+email);
            }
            return ResponseClass.responseSuccess("Notifications for user fetched successfully", "notifications", notifications);
        } List<NotificationHistory> notifications = notificationHistoryRepository.findAll();

        if (notifications.isEmpty()) {
            return ResponseClass.responseFailure("No notifications found");
        }
        return ResponseClass.responseSuccess("Notifications fetched successfully", "notifications", notifications);
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