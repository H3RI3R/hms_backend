package com.example.main.Subscribe.Service;

import com.example.main.Configuration.CacheService;
import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.GuestManagement.Model.Guest;
import com.example.main.GuestManagement.Repository.GuestRepo;
import com.example.main.Subscribe.Model.SubscriberMail;
import com.example.main.Subscribe.Repository.SubscriberRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.LongStream;

@Service
@RequiredArgsConstructor
public class SubscriberService {

    private static final Logger log = LoggerFactory.getLogger(SubscriberService.class);
    private final SubscriberRepo subscriberRepo;
    private final GuestRepo guestRepo;
    private final CacheService cacheService;


    private static final String SubscriberCacheKey = "subscriberCacheKey";

    public ResponseEntity<?> sendBulkMail(
            String hotelId, String subject, String body,
            Integer coolingPeriod, Long fromUserId, Long toUserId)
    {
        SubscriberMail subscriberMail = new SubscriberMail();
        subscriberMail.setHotelId(hotelId);
        subscriberMail.setSubject(subject);
        subscriberMail.setBody(body);
        subscriberMail.setCoolingPeriod(coolingPeriod);
        List<Long> guestIdRange= LongStream.range(fromUserId, toUserId+1).boxed().toList();
        for(Long guestId: guestIdRange) {
            Guest guest = guestRepo.findById(guestId).orElse(null);
            if(guest == null) continue;
            subscriberMail.getRecipientIds().add(guest.getId());
            subscriberMail.getRecipientEmails().add(guest.getEmail());
        }
        subscriberRepo.save(subscriberMail);
        sendMailToSubscriber(subscriberMail);
        return ResponseClass.responseSuccess("sending mails in progress");
    }


    private final ConfigClass configClass;
    public void sendMailToSubscriber(SubscriberMail subscriberMail) {
        for (String mailId : subscriberMail.getRecipientEmails()) {
            try {
                configClass.sendEmail(
                        mailId,
                        subscriberMail.getSubject(),
                        subscriberMail.getBody());
            } catch (Exception e) {
                log.info("failed to send mail on: "+ mailId);
            }
        }
    }


    public ResponseEntity<?> getSubscriberList(String hotelId) {
        String cacheKey = "hotelId:" + hotelId;
        List<GuestSubscriberRecordDTO> subscriberList = (List<GuestSubscriberRecordDTO>) cacheService.getFromHashCache(SubscriberCacheKey, cacheKey);

        if (subscriberList == null) {
            List<Guest> guestList = guestRepo.findByHotelIdAndIsSubscriber(hotelId, true);

            subscriberList = guestList.stream()
                .map(guest ->
                new GuestSubscriberRecordDTO(
                        guest.getId(), guest.getFirstName(),
                        guest.getLastName(), guest.getEmail(),
                        guest.getIsEmailVerified()))
                .toList();
            
                cacheService.putInHashCache(SubscriberCacheKey, cacheKey, subscriberList);
        }
        return ResponseClass.responseSuccess("guest list", "guestSubscriber", subscriberList);
    }

    public ResponseEntity<?> sendMailNotification( String subject, List<String> sentMail, String body, Integer coolingPeriod, Long fromUserId, Long batchUsers) {
        int successCount = 0;
        int failureCount = 0;
        for (String mailId : sentMail) {
            try {
                configClass.sendEmail(
                        mailId,
                        subject,
                        body
                );
                log.info("Email successfully sent to: " + mailId);
                successCount++;
                if (coolingPeriod != null && coolingPeriod > 0) {
                    Thread.sleep(coolingPeriod * 1000L); // Cooling period in seconds
                }
            } catch (Exception e) {
                log.error("Failed to send email to: " + mailId, e);
                failureCount++;
            }
        }

        //String responseMessage = String.format("Emails sent: %d, failed: %d", successCount, failureCount);
        //log.info(responseMessage);
        Map<String,Integer> responseMessage = new HashMap<String, Integer>();
        responseMessage.put("success", successCount);
        responseMessage.put("failure", failureCount);

        return ResponseEntity.ok(responseMessage);
    }


    record GuestSubscriberRecordDTO(Long guestId, String firstName, String lastName, String email, Boolean isEmailVerified){}
}
