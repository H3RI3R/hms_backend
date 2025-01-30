package com.example.main.Hotel.Controller;

import com.example.main.Hotel.DTO.PremiumDTO;
import com.example.main.Hotel.Service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/booking")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingController {

    private final BookingService bookingService;


    @GetMapping("/availRoomByRoomType")
    public ResponseEntity<?> findAvailableRooms(@RequestHeader("Authorization") String token,
                                                @RequestParam Long roomTypeId,
                                                @RequestParam Integer totalRoom,
                                                @RequestParam("checkInDate") LocalDateTime checkInDate,
                                                @RequestParam("checkOutDate")  LocalDateTime checkOutDate
    ) {
        return bookingService.findAvailableRoom(token, roomTypeId, totalRoom, checkInDate, checkOutDate);
    }

    @PostMapping("/bookingRoom")
    public ResponseEntity<?> bookRoomForWalkInGuest(@RequestHeader("Authorization") String token,
                                                    @RequestParam String bookingType,
                                                    @RequestParam List<Integer> roomNumbers,
                                                    @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkInDate,
                                                    @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOutDate,
                                                    @RequestParam(required = false) String guestName,
                                                    @RequestParam(required = false) String phone,
                                                    @RequestParam String email,
                                                    @RequestParam(required = false) Integer adult,
                                                    @RequestParam(required = false) Integer children,
                                                    @RequestParam(required = false) String address,
                                                    @RequestParam(required = false) Double totalPaid
    ) {
        return bookingService.roomBook(token, bookingType,
                roomNumbers,
                checkInDate,
                checkOutDate,
                guestName,
                phone,
                email,
                adult,
                children,
                address,
                totalPaid
        );

    }

    @PutMapping("/updateCheckOut/{bookingId}")
    public ResponseEntity<?> updateCheckOutDate(@RequestHeader("Authorization") String token,
                                                @PathVariable Long bookingId
//            , @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime checkOutDate
    ) {
        return bookingService.updateCheckOut(token, bookingId);
    }



    @GetMapping("/getAll")
    public ResponseEntity<?> getAll(@RequestHeader("Authorization") String token,
                                    @RequestParam(required = false) LocalDate startDate,
                                    @RequestParam(required = false) LocalDate endDate

    ) {
        return bookingService.getAllBookings(token,startDate,endDate);
    }

    @GetMapping("/getAllAvRoom")
    public ResponseEntity<?> getAllAvailableRoom(@RequestHeader("Authorization") String token) {
        return bookingService.getAllAvailableRoom(token);
    }


    @GetMapping("/todayCheckIns")
    public ResponseEntity<?> getTodayBookings(@RequestHeader("Authorization") String token) {
        return bookingService.getBookings(token);
    }

    @GetMapping("/todayCheckoutList")
    public ResponseEntity<?> getTodayCheckoutList(@RequestHeader("Authorization") String token) {
        return bookingService.getTodayCheckoutList(token);
    }

    @GetMapping("/activeBookings")
    public ResponseEntity<?> getActiveBookings(@RequestHeader("Authorization") String token,
                                               @RequestParam(required = false) LocalDate startDate,
                                               @RequestParam(required = false) LocalDate endDate

                                               ) {
        return bookingService.getActiveBookings(token,startDate,endDate);
    }

    @GetMapping("/checkoutBookings")
    public ResponseEntity<?> getCheckoutBookings(@RequestHeader("Authorization") String token) {
        return bookingService.getCheckoutBookings(token);
    }


    @GetMapping("/todayBookRoom")
    public ResponseEntity<?> getTodayBookedRoomNumbers(@RequestHeader("Authorization") String token) {
        return bookingService.getTodayBookedRoomNumbers(token);
    }


    @PostMapping("/addService")
    public ResponseEntity<?> addPremiumBooking(@RequestHeader("Authorization") String token,
                                               @RequestParam LocalDate date,
                                               @RequestParam int roomNo,
                                               @RequestBody List<PremiumDTO> premiumServiceList


    ) {
        return bookingService.addPremiumBooking(token, date, roomNo,premiumServiceList);
    }



    @GetMapping("/getPremBookingId/{bookingId}")
    public ResponseEntity<?> getPremiumBookingId(@RequestHeader("Authorization") String token,@PathVariable String bookingId) {
        return bookingService.getPremiumBookingId(token,bookingId);
    }

    @GetMapping("/getDelayedCheckOut")
    public ResponseEntity<?> getDelayedCheckOut(@RequestHeader("Authorization") String token,
                                                @RequestParam(required = false) LocalDate startDate,
                                                @RequestParam(required = false) LocalDate endDate

    ) {

        return bookingService.getDelayedCheckOut(token,startDate,endDate);
    }


    @GetMapping("/getUpcomingCheckOut")
    public ResponseEntity<?> getUpcomingCheckOut(@RequestHeader("Authorization") String token) {
        return bookingService.getUpComingCheckOut(token);
    }

    @GetMapping("/getBookedRoomInAllBooking")
    public ResponseEntity<?> getBookedRoomInAllBooking(@RequestHeader("Authorization") String token) {
        return bookingService.getBookedRoomInAllBooking(token);
    }

    @GetMapping("/getBookedRoomByBookingId/{bookingId}")
    public ResponseEntity<?> getBookedRoomByBookingId(@RequestHeader("Authorization") String token,
                                                      @PathVariable long bookingId) {
        return bookingService.getBookedRoomByBookingId(token, bookingId);
    }

    @GetMapping("/getByBookingNo/{bookingNo}")
    public ResponseEntity<?> getByBookingNo(@RequestHeader("Authorization") String token,@PathVariable String bookingNo) {
        return bookingService.getByBookingNo(token,bookingNo);
    }

    @GetMapping("/getByBookingId/{bookingId}")
    public ResponseEntity<?> getByBookingId(@RequestHeader("Authorization") String token,@PathVariable long bookingId) {
        return bookingService.getByBookingId(token,bookingId);
    }
    @GetMapping("/getBookingDataByBookingId/{bookingId}")
    public ResponseEntity<?> getBookingDataBId(@RequestHeader("Authorization") String token,@PathVariable long bookingId) {
        return bookingService.getBookingDataByBookingId(token,bookingId);
    }



    @DeleteMapping("/delete/{bookingId}")
    public ResponseEntity<?> deleteById(@RequestHeader("Authorization") String token,
                                        @PathVariable long bookingId) {
        return bookingService.deleteById(token,bookingId);
    }

//    @PostMapping("/bookingCancel/{bookingNo}")
//    public ResponseEntity<?> cancelBooking(@RequestHeader("Authorization") String token,
//                                         @PathVariable String bookingId) {
//        return bookingService.cancelBooking(token, bookingId);
//    }

    @GetMapping("/getCancelBooking")
    public ResponseEntity<?> getCancelBooking(@RequestHeader("Authorization") String token,
                                              @RequestParam(required = false) LocalDate startDate,
                                              @RequestParam(required = false) LocalDate endDate

    ) {
        return bookingService.getCancelBooking(token,startDate,endDate);
    }

    @GetMapping("/getAllCheckout")
    public ResponseEntity<?> getAllCheckout(@RequestHeader("Authorization") String token,
                                            @RequestParam(required = false) LocalDate startDate,
                                            @RequestParam(required = false) LocalDate endDate

    ) {
        return bookingService.getAllCheckOut(token,startDate,endDate);
    }

    @GetMapping("/getAllRefundable")
    public ResponseEntity<?> getAllRefundable(@RequestHeader("Authorization") String token,
                                              @RequestParam(required = false) LocalDate startDate,
                                              @RequestParam(required = false) LocalDate endDate

    ) {
        return bookingService.getAllRefundable(token,startDate,endDate);
    }

    @GetMapping("/getTodayCheckOut")
    public ResponseEntity<?> getTodayCheckOut(@RequestHeader("Authorization") String token) {
        return bookingService.getTodayCheckOut(token);
    }

    @GetMapping("/getAllPremBooking")
    public ResponseEntity<?> getAllPremBooking(@RequestHeader("Authorization") String token) {
        return bookingService.getAllPremBooking(token);
    }

    @GetMapping("/getPremBookingById/{bookingId}")
    public ResponseEntity<?> getPremBookingById(@RequestHeader("Authorization") String token,@PathVariable long bookingId) {
        return bookingService.getPremBookingById(token,bookingId);
    }


    @GetMapping("/getPendingCheckIn")
    public ResponseEntity<?> getPendingCheckIn(@RequestHeader("Authorization") String token) {
        return bookingService.getPendingCheckIn(token);
    }

    @GetMapping("/getUpcomingCheckIn")
    public ResponseEntity<?> getUpcomingCheckIn(@RequestHeader("Authorization") String token) {
        return bookingService.getUpcomingCheckIn(token);
    }

    @PutMapping("/bookingCancelById/{bookingId}")
    public ResponseEntity<?> bookingCancelById(@RequestHeader("Authorization") String token,@PathVariable long bookingId) {
        return bookingService.bookingCancelById(token,bookingId);
    }

    @GetMapping("/getCancelBookingById/{bookingId}")
    public ResponseEntity<?> getCancelBookingById(@RequestHeader("Authorization") String token,@PathVariable long bookingId) {
        return bookingService.getCancelBookingById(token,bookingId);
    }




}
