package com.example.main.Report.Service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.Hotel.Entity.Hotel;
import com.example.main.Hotel.Repo.HotelRepo;
import com.example.main.Report.Entity.BookingAction;
import com.example.main.Report.Repo.BookingActionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingActionsService {
    private ConfigClass configClass;
    @Autowired
    private HotelRepo hotelRepo;
    @Autowired
    private BookingActionRepository bookingActionRepository;

    public ResponseEntity<?> getActionReportByBookingNo( String bookingNo) {
        List<BookingAction> bookingActions = bookingActionRepository.findByBookingNo(bookingNo);

        if (bookingActions.isEmpty()) {
            return ResponseClass.responseFailure("No actions found for this booking.");
        }

        return ResponseClass.responseSuccess("Action report fetched successfully", "bookingActions", bookingActions);
    }
    public void saveBookingAction(String bookingNo, String actionBy, LocalDateTime date) {
        BookingAction bookingAction = new BookingAction();
        bookingAction.setBookingNo(bookingNo);
        bookingAction.setActionBy(actionBy);
        bookingAction.setDate(date);
        bookingActionRepository.save(bookingAction);
    }
    public List<BookingAction> getAllBookingActions() {
        return bookingActionRepository.findAll();
    }

}
