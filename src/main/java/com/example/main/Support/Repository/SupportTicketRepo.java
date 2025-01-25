package com.example.main.Support.Repository;

import com.example.main.Support.Model.SupportTicket;
import com.example.main.Support.Model.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupportTicketRepo extends JpaRepository<SupportTicket, Long>{

    SupportTicket findByTicketNumber(String ticketNo);

    List<SupportTicket> findByHotelIdOrderByAssignedAtDesc(String hotelId);

    List<SupportTicket> findByHotelIdAndStatus(String hotelId, TicketStatus name);

    List<SupportTicket> findByHotelId(String hotelId);
}
