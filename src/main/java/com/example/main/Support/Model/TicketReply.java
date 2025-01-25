package com.example.main.Support.Model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

@Data
@Entity
public class TicketReply implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @Enumerated(EnumType.STRING)
//    private ReplyBy replyBy;
    private String replyByUserType;
    private String reply;
    private Instant createdAt;

    @ManyToOne
    private SupportTicket supportTicket;

}
