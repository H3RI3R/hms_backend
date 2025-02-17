package com.example.main.Support.Controller;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Support.Model.TicketStatus;
import com.example.main.Support.Service.SupportTicketService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/support")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SupportTicketController {

    private static final Logger log = LoggerFactory.getLogger(SupportTicketController.class);
    private final SupportTicketService supportTicketService;
    private final ConfigClass configClass;

    @PostMapping("/createTicket")
    public ResponseEntity<?> replyToTicket(
            // @RequestHeader("Authorization") String token,
            @RequestParam("subject") String subject,
            @RequestParam("description") String description,
            @RequestParam("bookingNo") String bookingNo,
            @RequestParam("userEmail") String userEmail,
            @RequestParam(required = false) MultipartFile file1,
            @RequestParam(required = false) MultipartFile file2,
            @RequestParam(required = false) MultipartFile file3,
            @RequestParam(required = false) MultipartFile file4,
            @RequestParam(required = false) MultipartFile file5) {
        List<MultipartFile> files = new ArrayList<>();
        if (file1 != null) files.add(file1);
        if (file2 != null) files.add(file2);
        if (file3 != null) files.add(file3);
        if (file4 != null) files.add(file4);
        if (file5 != null) files.add(file5);
        // String hotelId = configClass.tokenValue(token, "hotelId");
        return supportTicketService.createTicket(
        subject, description, bookingNo, userEmail, files);
    }


    @PutMapping("/updateTicket/{ticketNo}")
    public ResponseEntity<?> updateTicket(
            @RequestHeader("Authorization") String token,
            @PathVariable String ticketNo,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) Long assignedTo,
            @RequestParam(required = false) String comment,
            @RequestParam(required = false) Boolean isClosed,
            @RequestParam(required = false) MultipartFile file1,
            @RequestParam(required = false) MultipartFile file2,
            @RequestParam(required = false) MultipartFile file3,
            @RequestParam(required = false) MultipartFile file4,
            @RequestParam(required = false) MultipartFile file5
    ) {
        Map<String, MultipartFile> files = new HashMap<>();
        if (file1 != null) files.put("file1",file1);
        if (file2 != null) files.put("file2",file2);
        if (file3 != null) files.put("file3",file3);
        if (file4 != null) files.put("file4",file4);
        if (file5 != null) files.put("file5",file5);
        log.info("ticketNo: " + ticketNo);
        log.info("files: " + files);
        // String userId = configClass.tokenValue(token, "userId");
//        Long assignedBy = Long.parseLong(userId);
        return supportTicketService.updateTicket(ticketNo, status, priority, comment, isClosed, files, assignedTo); //assignedBy);
    }

    @GetMapping("/getRelatedGuest/{bookingId}")
    public ResponseEntity<?> getRelatedGuest(
            @PathVariable String bookingId) {
        return supportTicketService.getRelatedGuest(bookingId);
    }

    @GetMapping("/getTicket/{ticketNo}")
    public ResponseEntity<?> getTicket(
            @PathVariable("ticketNo") String ticketId) {
        return supportTicketService.getTicket(ticketId);
    }

    @GetMapping("/getAllTickets")
    public ResponseEntity<?> getAllTickets(
            @RequestHeader("Authorization") String token,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(value = "search" , required = false) String email
    ) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        String roleType = configClass.tokenValue(token, "roleType");
        System.out.println(roleType);
        return supportTicketService.getAllTickets(hotelId, page, size , email);
    }


    @GetMapping("/getTicketsByStatus")
    public ResponseEntity<?> getTicketsByStatus(
            @RequestHeader("Authorization") String token,
            @RequestParam("status") TicketStatus status
    ) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return supportTicketService.getTicketsByStatus(hotelId, status);
    }
    @GetMapping("/getPendingTickets")
    public ResponseEntity<?> getPendingTickets(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "search", required = false) String email
    ) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return supportTicketService.getPendingTickets(hotelId,email);
    }
    @GetMapping("/getClosedTickets")
    public ResponseEntity<?> getClosedTickets(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "search", required = false) String email
    ) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return supportTicketService.getClosedTickets(hotelId ,email);
    }
    @GetMapping("/getOpenTickets")
    public ResponseEntity<?> getOpenTickets(
            @RequestHeader("Authorization") String token
    ) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return supportTicketService.getOpenTickets(hotelId);
    }

    @GetMapping("/getAnsTickets")
    public ResponseEntity<?> getAnsTickets(
            @RequestHeader("Authorization") String token ,
            @RequestParam(value = "search" ,required = false )String email
    ) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return supportTicketService.getAnsTickets(hotelId ,email);
    }

    @PostMapping("/replyToTicket/{ticketNo}")
    public ResponseEntity<?> replyToTicket(
            @RequestHeader("Authorization") String token,
            @PathVariable("ticketNo") String ticketNo,
            @RequestParam("reply") String reply,
            @RequestParam(required = false) MultipartFile file) {
        String roleType = configClass.tokenValue(token, "roleType");
        return supportTicketService.replyToTicket(ticketNo, reply, roleType,file);
    }


    @PostMapping("/closeTicket/{ticketNo}")
    public ResponseEntity<?> closeTicket(
            @RequestHeader("Authorization") String token,
            @PathVariable("ticketNo") String ticketNo) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return supportTicketService.closeTicket(ticketNo, hotelId);
    }


    @DeleteMapping("/deleteTicket/{ticketNo}")
    public ResponseEntity<?> deleteTicket(
            @RequestHeader("Authorization") String token,
            @PathVariable("ticketNo") String ticketId) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return supportTicketService.deleteTicket(ticketId, hotelId);
    }









}
