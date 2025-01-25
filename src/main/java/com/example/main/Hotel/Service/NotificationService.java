package com.example.main.Hotel.Service;


import com.example.main.Configuration.NotificationWebSocketHandler;
import com.example.main.Configuration.SystemUsageMonitor;
import com.example.main.Hotel.Entity.Notification;
import com.example.main.Hotel.Repo.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class NotificationService {

    @Autowired
    private NotificationWebSocketHandler notificationWebSocketHandler;

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendNotification(String message) {
        try {
            notificationWebSocketHandler.sendNotification(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendHotelAddedNotification(String hotelName) {

        String message = "A new hotel has been added: " + hotelName;
        try {
            notificationWebSocketHandler.sendNotification(message);

            // Persist notification in the database
            Notification notification = new Notification();
            notification.setMessage(message);

            notification.setTimestamp(LocalDateTime.now());
            notification.setType("HOTEL_ADDED");
            notificationRepository.save(notification);
        } catch (IOException e) {
            // Handle the exception, e.g., log it
            e.printStackTrace();
        }
    }


}