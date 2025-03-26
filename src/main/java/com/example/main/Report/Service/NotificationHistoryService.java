package com.example.main.Report.Service;
import com.example.main.Exception.ResponseClass;
import com.example.main.Report.Entity.NotificationHistory;
import com.example.main.Report.Repo.NotificationHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public ResponseEntity<Map<String, Object>> getAllNotifications(
            String searchKey, String fromDate, String toDate, Integer page, Integer size) {

        if ((fromDate != null && toDate == null) || (toDate != null && fromDate == null)) {
            return ResponseClass.responseFailure("Both fromDate and toDate must be provided together.");
        }

        if ((page != null && size == null) || (size != null && page == null)) {
            return ResponseClass.responseFailure("Both page and size must be provided together.");
        }

        List<NotificationHistory> notifications;
        Pageable pageable = null;
        Page<NotificationHistory> pagedResult = null;

        if (page != null && size != null) {
            pageable = PageRequest.of(page, size, Sort.by("dateTime").descending());
        }

        if (searchKey != null && !searchKey.trim().isEmpty()) {
            if (isValidEmail(searchKey)) {
                if (pageable != null) {
                    pagedResult = notificationHistoryRepository.findByUserEmail(searchKey, pageable);
                    notifications = pagedResult.getContent();
                } else {
                    notifications = notificationHistoryRepository.findByUserEmail(searchKey);
                }
            } else {
                return ResponseClass.responseFailure("Invalid email format: " + searchKey);
            }
        } else if (fromDate != null && toDate != null) {
            LocalDateTime start = parseDateTime(fromDate);
            LocalDateTime end = parseDateTime(toDate);
            if (pageable != null) {
                pagedResult = notificationHistoryRepository.findByDateTimeBetween(start, end, pageable);
                notifications = pagedResult.getContent();
            } else {
                notifications = notificationHistoryRepository.findByDateTimeBetween(start, end);
            }
        } else {
            if (pageable != null) {
                pagedResult = notificationHistoryRepository.findAll(pageable);
                notifications = pagedResult.getContent();
            } else {
                notifications = notificationHistoryRepository.findAll();
            }
        }

        if (notifications.isEmpty()) {
            return ResponseClass.responseFailure("No notifications found");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Notifications fetched successfully");
        response.put("notifications", notifications);

        if (pagedResult != null) {
            response.put("totalElements", pagedResult.getTotalElements());
            response.put("currentPage", pagedResult.getNumber());
            response.put("totalPages", pagedResult.getTotalPages());
            response.put("pageSize", pagedResult.getSize());
        }

        return ResponseEntity.ok(response);
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(emailRegex).matcher(email).matches();
    }

    private LocalDateTime parseDateTime(String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(dateStr + " 00:00:00", formatter);
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