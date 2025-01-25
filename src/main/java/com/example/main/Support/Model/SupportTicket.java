package com.example.main.Support.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Map;

@Data
@Entity
public class SupportTicket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long  id;

    private String ticketNumber;

    private String hotelId;

    private String subject;

    private String description;

    private String bookingNo;

    private String userEmail;

    @Enumerated(EnumType.STRING)
    private TicketStatus status = TicketStatus.OPEN;

    private String priority;

    private Instant createdAt;

    private Instant updatedAt;

    private Boolean isClosed;

    private Instant closedAt;

    private Long assignedToStaff;

    private Instant assignedAt;

    private Long assignedByStaff;

    private String comment;

    @ElementCollection
    @CollectionTable
    @MapKeyColumn
    private Map<String, String> ticketFiles;

//    @OneToMany
//    private List<TicketReply> ticketReplyList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "supportTicket")
    private List<TicketReply> ticketReplyList;


}
