package com.example.main.Support.Service;

import com.example.main.Configuration.CacheService;
import com.example.main.Configuration.ConfigClass;
import com.example.main.Configuration.PaginationUtils;
import com.example.main.Exception.ResponseClass;
import com.example.main.GuestManagement.Model.Guest;
import com.example.main.GuestManagement.Repository.GuestRepo;
import com.example.main.Hotel.Entity.Booking;
import com.example.main.Hotel.Repo.BookingRepo;
import com.example.main.Support.DTO.SupportTicketDTO;
import com.example.main.Support.Model.ReplyBy;
import com.example.main.Support.Model.SupportTicket;
import com.example.main.Support.Model.TicketReply;
import com.example.main.Support.Model.TicketStatus;
import com.example.main.Support.Repository.SupportTicketRepo;
import com.example.main.Support.Repository.TicketReplyRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class SupportTicketService {

    private final GuestRepo guestRepo;
    private final BookingRepo bookingRepo;
    private final SupportTicketRepo supportTicketRepo;
    private final TicketReplyRepo ticketReplyRepo;
    private final CacheService cacheService;

    private static final String SupportTicketCacheKey = "SupportTicketCacheKey";


    public ResponseEntity<?> createTicket(String subject, String description, String bookingNo, String userEmail, List<MultipartFile> files) {
        SupportTicket ticket = new SupportTicket();
        ticket.setSubject(subject);
        ticket.setDescription(description);
        ticket.setBookingNo(bookingNo);
        ticket.setUserEmail(userEmail);
        Booking findBooking = bookingRepo.findByBookingNo(ticket.getBookingNo());
        if (findBooking == null) {
            return ResponseClass.responseFailure("booking not found");
        }
        // if(!findBooking.getHotelId().equals(hotelId)){
        //     return ResponseClass.responseFailure("Invalid Hotel");
        // }
        ticket.setHotelId(findBooking.getHotelId());
        ticket.setCreatedAt(Instant.now());
        String ticketNo = ConfigClass.idCreate(findBooking.getHotelId(),"Ticket");
        ticket.setTicketNumber(ticketNo);
        ticket.setStatus(TicketStatus.OPEN);
        String imageStatus = "";
        if(!files.isEmpty()){
            int count = 1;
            for(MultipartFile file : files){
                try{
                    String path = ConfigClass.saveImage(file);
                    ticket.getTicketFiles().put("file"+count, path);
                    count++;
                }catch (Exception e){
                    imageStatus = "failed to save File";
                }
            }
        }
        String response;
        if(!imageStatus.isEmpty() || !imageStatus.isBlank()){
            response = "Ticket Created: "+imageStatus;
        }else {
            response = "Ticket Created successfully";
        }
        supportTicketRepo.save(ticket);
        return ResponseClass.responseSuccess(response);
    }



    public ResponseEntity<?> updateTicket(String ticketNo, TicketStatus status, String priority, String comment, Boolean isClosed, Map<String, MultipartFile> files, Long assignedTo) {
        SupportTicket supportTicket = supportTicketRepo.findByTicketNumber(ticketNo);
        supportTicket.setStatus(status != null ? status : supportTicket.getStatus());

        supportTicket.setPriority(priority != null ? priority : supportTicket.getPriority());
        supportTicket.setComment(comment != null ? comment : supportTicket.getComment());
        if((isClosed != null) && isClosed){
            supportTicket.setClosedAt(Instant.now());
            supportTicket.setIsClosed(true);
        }
        if(assignedTo != null) {
            supportTicket.setAssignedToStaff(assignedTo);
//            supportTicket.setAssignedByStaff(assignedBy);
        }
        if(!files.isEmpty()){
            for(Map.Entry<String, MultipartFile> file : files.entrySet()) {
                try {
                    String updatedPath = ConfigClass.saveImage(file.getValue(), supportTicket.getTicketFiles().get(file.getKey()));
                    supportTicket.getTicketFiles().put(file.getKey(), updatedPath);
                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseClass.responseFailure("failed to save File");
                }
            }
        }
        SupportTicketDTO updatedTicket = new SupportTicketDTO(supportTicketRepo.save(supportTicket));
        return ResponseClass.responseSuccess("Ticket updated successfully", "ticket", updatedTicket);
    }


    public ResponseEntity<?> getRelatedGuest(String bookingId) {
        Guest guest = guestRepo.findByBookingId(bookingId);
        if(guest == null) return ResponseClass.responseFailure("guest not found");
        return ResponseClass.responseSuccess("guest information","guest",guest);
    }


    public ResponseEntity<?> getTicket(String ticketNo) {
        SupportTicket supportTicket = supportTicketRepo.findByTicketNumber(ticketNo);
        if(supportTicket == null) return ResponseClass.responseFailure("Ticket not found");
        return ResponseClass.responseSuccess("Ticket information", "ticket", new SupportTicketDTO(supportTicket));
    }

    public ResponseEntity<?> getAllTickets(String hotelId, Integer page, Integer size , String email) {
        // Define the cache key
        List<SupportTicket>supportTickets;
        String cacheKey = "hotelId:" + hotelId;
        if (email != null && !email.isEmpty()) {
            supportTickets = supportTicketRepo.findByHotelIdAndUserEmail(hotelId,email);
        }
        else {
            supportTickets = supportTicketRepo.findByHotelIdOrderByAssignedAtDesc(hotelId);
        }
        // Try to retrieve the data from Redis cache
        //List<SupportTicketDTO> supportTicketDTOS = (List<SupportTicketDTO>) cacheService.getFromHashCache(SupportTicketCacheKey, cacheKey);

//       supportTickets = supportTicketRepo.findByHotelIdOrderByAssignedAtDesc(hotelId);
//        List<SupportTicket> supportTickets = supportTicketRepo.findByHotelId(hotelId);
//        System.out.println(supportTickets.size() + " supportTickets");
        // Check if no tickets are found
        if (supportTickets.isEmpty()) {
            return ResponseClass.responseFailure("No tickets found");
        }

        // Map the list of SupportTicket entities to SupportTicketDTOs
        List<SupportTicketDTO> supportTicketDTOS = supportTickets.stream()
                .map(SupportTicketDTO::new)
                .toList();

        // Apply pagination if page and size parameters are provided
        if (page != null && size != null) {
            supportTicketDTOS = PaginationUtils.getPaginatedList(supportTicketDTOS, page, size);
        }

        // Return the response with the DTOs
        return ResponseClass.responseSuccess("All tickets", "tickets", supportTicketDTOS);
    }




    public ResponseEntity<?> getTicketsByStatus(String hotelId, TicketStatus status) {
        List<SupportTicket> supportTickets = supportTicketRepo.findByHotelIdAndStatus(hotelId, status);
        if(supportTickets.isEmpty()){
            return ResponseClass.responseFailure("No tickets found");
        }
        List<SupportTicketDTO> supportTicketDTOS = supportTickets.stream().map(SupportTicketDTO::new).toList();
        return ResponseClass.responseSuccess("All tickets", "tickets", supportTicketDTOS);
    }


    @Transactional
    public ResponseEntity<?> replyToTicket(String ticketId, String reply, String roleType,MultipartFile file) {
        SupportTicket supportTicket = supportTicketRepo.findByTicketNumber(ticketId);
        if(supportTicket == null){
            return ResponseClass.responseFailure("Ticket not found");
        }
        if(supportTicket.getStatus() == TicketStatus.CLOSED){
            return ResponseClass.responseFailure("Ticket is closed");
        }
//        if(!supportTicket.getAssignedToStaff().equals(userId)){
//            return ResponseClass.responseFailure("You are not assigned to this ticket");
//        }
        TicketReply ticketReply = new TicketReply();
        ticketReply.setReply(reply);
        ticketReply.setSupportTicket(supportTicket);
        ticketReply.setReplyByUserType(roleType);

//        switch (roleType) {
//            case "SUPERADMIN":
//                ticketReply.setReplyBy(ReplyBy.SUPERADMIN);
//                break;
//            case "ADMIN":
//                ticketReply.setReplyBy(ReplyBy.ADMIN);
//                break;
//            case "STAFF":
//                ticketReply.setReplyBy(ReplyBy.STAFF);
//                break;
//            case "USER":
//                ticketReply.setReplyBy(ReplyBy.USER);
//                break;
//            default:
//                ticketReply.setReplyBy(ReplyBy.STAFF);  // Default to STAFF if roleType is invalid
//        }


        ticketReply.setCreatedAt(Instant.now());
        ticketReplyRepo.save(ticketReply);

        supportTicket.getTicketReplyList().add(ticketReply);
        supportTicketRepo.save(supportTicket);
        //SupportTicketDTO supportTicketDTO = new SupportTicketDTO(supportTicket);
        return ResponseClass.responseSuccess("Reply added successfully");
    }

    public ResponseEntity<?> closeTicket(String ticketId, String hotelId) {
        SupportTicket supportTicket = supportTicketRepo.findByTicketNumber(ticketId);
        if(supportTicket == null){
            return ResponseClass.responseFailure("Ticket not found");
        }
        if(!supportTicket.getHotelId().equals(hotelId)){
            return ResponseClass.responseFailure("unauthorised");
        }
        supportTicket.setStatus(TicketStatus.CLOSED);
        supportTicket.setClosedAt(Instant.now());
        supportTicket.setIsClosed(true);
        supportTicketRepo.save(supportTicket);
        return ResponseClass.responseSuccess(supportTicket.getStatus().toString());
    }

    public ResponseEntity<?> getPendingTickets(String hotelId , String email) {
        List<SupportTicket> supportTickets = supportTicketRepo.findByHotelIdAndStatusAndUserEmail(hotelId, TicketStatus.PENDING , email);
        if(supportTickets.isEmpty()){
            return ResponseClass.responseFailure("No tickets found");
        }
        List<SupportTicketDTO> supportTicketDTOS = supportTickets.stream().map(SupportTicketDTO::new).toList();
        return ResponseClass.responseSuccess("All tickets", "tickets", supportTicketDTOS);
    }

    public ResponseEntity<?> getClosedTickets(String hotelId ,String email) {
        List<SupportTicket> supportTickets = supportTicketRepo.findByHotelIdAndStatusAndUserEmail(hotelId, TicketStatus.CLOSED ,email);
        if(supportTickets.isEmpty()){
            return ResponseClass.responseFailure("No tickets found");
        }
        List<SupportTicketDTO> supportTicketDTOS = supportTickets.stream().map(SupportTicketDTO::new).toList();
        return ResponseClass.responseSuccess("All tickets", "tickets", supportTicketDTOS);
    }

    public ResponseEntity<?> getOpenTickets(String hotelId) {
        List<SupportTicket> supportTickets = supportTicketRepo.findByHotelIdAndStatus(hotelId, TicketStatus.OPEN);
        if(supportTickets.isEmpty()){
            return ResponseClass.responseFailure("No tickets found");
        }
        List<SupportTicketDTO> supportTicketDTOS = supportTickets.stream().map(SupportTicketDTO::new).toList();
        return ResponseClass.responseSuccess("All tickets", "tickets", supportTicketDTOS);
    }

    public ResponseEntity<?> deleteTicket(String ticketId, String hotelId) {

        SupportTicket supportTicket = supportTicketRepo.findByTicketNumber(ticketId);
        if(supportTicket == null){
            return ResponseClass.responseFailure("Ticket not found");
        }
        if(!supportTicket.getHotelId().equals(hotelId)){
            return ResponseClass.responseFailure("unauthorised");
        }
        supportTicketRepo.delete(supportTicket);
        return ResponseClass.responseSuccess("Ticket deleted successfully");
    }

    public ResponseEntity<?> getAnsTickets(String hotelId, String email) {
    List<SupportTicket> supportTickets = supportTicketRepo.findByHotelIdAndStatusAndUserEmail(hotelId, TicketStatus.ANSWERED , email);
    if(supportTickets.isEmpty()){
        return ResponseClass.responseFailure("No tickets found");
    }
    return ResponseClass.responseSuccess("get all answer ticket","answerTicket",supportTickets);
    }
}
