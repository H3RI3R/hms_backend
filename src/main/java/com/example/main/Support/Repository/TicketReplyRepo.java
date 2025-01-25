package com.example.main.Support.Repository;

import com.example.main.Support.Model.TicketReply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketReplyRepo extends JpaRepository<TicketReply, Long> {

}
