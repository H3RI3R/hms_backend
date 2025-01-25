package com.example.main.Support.DTO;

import com.example.main.Support.DateTimeWeekDay;
import com.example.main.Support.Model.TicketReply;
import lombok.Data;

import java.io.Serializable;


@Data
public class TicketReplyDTO implements Serializable {
    private Long id;
    private String replyBy;
    private String reply;
    private DateTimeWeekDay createdAt;

    public TicketReplyDTO(TicketReply ticketReply) {
        this.id = ticketReply.getId();
//        this.replyBy = ticketReply.getReplyBy().name(); // Convert ReplyBy enum to String
        this.reply = ticketReply.getReply();
        this.createdAt = new DateTimeWeekDay(ticketReply.getCreatedAt());
    }

}
