package com.example.main.Subscribe.Controller;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Subscribe.Service.SubscriberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscribe")
@RequiredArgsConstructor
public class SubscriberController {
    private final ConfigClass configClass;
    private final SubscriberService subscriberService;

    @PostMapping("/sendBulkMail")
    public ResponseEntity<?> sendBulkMail(
            @RequestHeader("Authorization") String token,
            @RequestParam String subject,
            @RequestParam String body,
            @RequestParam Integer coolingPeriod,
            @RequestParam Long fromUserId,
            @RequestParam Long batchUsers){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return subscriberService.sendBulkMail(hotelId, subject, body, coolingPeriod, fromUserId, batchUsers);
    }


    @PostMapping("/sendMailNotification")
    public ResponseEntity<?> sendMailNotification(
//            @RequestHeader("Authorization") String token,
            @RequestParam String subject,
            @RequestParam List<String> sentMail,
            @RequestParam String body,
            @RequestParam Integer coolingPeriod,
            @RequestParam Long fromUserId,
            @RequestParam Long batchUsers){
       // String hotelId = configClass.tokenValue(token, "hotelId");
        return subscriberService.sendMailNotification( subject, sentMail,body, coolingPeriod, fromUserId, batchUsers);
    }


    @GetMapping("/getSubscriberList")
    public ResponseEntity<?> getBulkMail(
            @RequestHeader("Authorization") String token){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return subscriberService.getSubscriberList(hotelId);
    }



}
