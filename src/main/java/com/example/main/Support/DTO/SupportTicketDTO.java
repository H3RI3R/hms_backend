package com.example.main.Support.DTO;

import com.example.main.Support.DateTimeWeekDay;
import com.example.main.Support.Model.SupportTicket;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
public class SupportTicketDTO implements Serializable {
    private Long id;
    private String ticketNumber;
    private String hotelId;
    private String subject;
    private String description;
    private String bookingNo;
    private String userEmail;
    private String status;
    private String priority;
    private DateTimeWeekDay createdAt;
    private Instant updatedAt;
    private Boolean isClosed;
    private Instant closedAt;
    private Long assignedToStaff;
    private Instant assignedAt;
    private Long assignedByStaff;
    private String comment;
    private Map<String, String> ticketFileUrl;
    private List<TicketReplyDTO> ticketReplyList;

    public SupportTicketDTO(SupportTicket supportTicket) {
        this.id = supportTicket.getId();
        this.ticketNumber = supportTicket.getTicketNumber();
        this.hotelId = supportTicket.getHotelId();
        this.subject = supportTicket.getSubject();
        this.description = supportTicket.getDescription();
        this.bookingNo = supportTicket.getBookingNo();
        this.userEmail = supportTicket.getUserEmail();
        this.status = supportTicket.getStatus().name(); // Convert TicketStatus to String
        this.priority = supportTicket.getPriority();
        this.createdAt = new DateTimeWeekDay(supportTicket.getCreatedAt());
        this.updatedAt = supportTicket.getUpdatedAt();
        this.isClosed = supportTicket.getIsClosed();
        this.closedAt = supportTicket.getClosedAt();
        this.assignedToStaff = supportTicket.getAssignedToStaff();
        this.assignedAt = supportTicket.getAssignedAt();
        this.assignedByStaff = supportTicket.getAssignedByStaff();
        this.comment = supportTicket.getComment();
        this.ticketFileUrl = supportTicket.getTicketFiles();
        this.ticketReplyList = supportTicket.getTicketReplyList().stream().map(TicketReplyDTO::new).toList();
    }

}
