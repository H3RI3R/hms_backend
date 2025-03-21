package com.example.main.Hotel.Service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.GuestManagement.Model.Guest;
import com.example.main.GuestManagement.Repository.GuestRepo;
import com.example.main.Hotel.DTO.PremiumDTO;
import com.example.main.Hotel.Entity.Booking;
import com.example.main.Hotel.Entity.BookingPremium;
import com.example.main.Hotel.Entity.Hotel;
import com.example.main.Hotel.Repo.BookingPremiumRepo;
import com.example.main.Hotel.Repo.BookingRepo;
import com.example.main.Hotel.Repo.HotelRepo;
import com.example.main.ManageHotel.Entity.Room;
import com.example.main.ManageHotel.Entity.RoomTypes;
import com.example.main.ManageHotel.Repo.RoomRepo;
import com.example.main.ManageHotel.Repo.RoomTypesRepo;
import com.example.main.Payment.Repo.PaymentRepo;
import com.example.main.Payment.entity.PaymentClass;
import com.example.main.Payment.entity.PaymentStatus;
import com.example.main.Report.Service.BookingActionsService;
import com.example.main.Report.Service.ReceivedPaymentsService;
import com.example.main.Report.Service.ReturnedPaymentsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingService {

    private final BookingRepo bookingRepo;
    private final ConfigClass configClass;
    private final RoomRepo roomRepo;
    private final RoomTypesRepo roomTypesRepo;
    private final HotelRepo hotelRepo;
    private final GuestRepo guestRepo;
    private final BookingPremiumRepo bookingPremiumRepo;
    private final PaymentRepo paymentRepo;
    @Autowired
    private ReturnedPaymentsService returnedPaymentsService;
    @Autowired
    private BookingActionsService bookingActionService;
    @Autowired
    private ReceivedPaymentsService receivedPaymentsService;



    public ResponseEntity<?> findAvailableRoom(String token, Long roomTypeId, Integer totalRoom, LocalDateTime checkInDate, LocalDateTime checkOutDate) {
        String hotelId =  configClass.tokenValue(token,"hotelId");
       // int numberOfDays = (int) Duration.between(checkInDate, checkOutDate).toDays();
        try {
//            for (int i = 0; i < numberOfDays; i++) {
//
//                LocalDate currentDate = checkInDate.plusDays(i);
//                LocalDate nextDate = currentDate.plusDays(1);
//                String dateRange = currentDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + " to " + nextDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
//                RoomTypes roomTypes = roomTypesRepo.findByRoomTypesId(roomTypeId);
//                List<Room> availableRooms = roomRepo.findAvailableRoom(roomTypes.getRoomName(),hotelId);
//
//                if (totalRoom != null && totalRoom > 0 && totalRoom > availableRooms.size()) {
//                    return ResponseClass.responseSuccess("not enough room available");
//                }
//                availableRoomsForDays.put(dateRange, availableRooms);
//            }

            RoomTypes roomTypes = roomTypesRepo.findByRoomTypesId(roomTypeId);
            int numberOfDays = (int) Duration.between(checkInDate, checkOutDate).toDays();

            // Fetch available rooms based on room type and hotel ID
            List<Room> availableRooms = roomRepo.findByRoomTypeAndHotelIdAndAvailableStatusTrueAndStatusTrue(
                    roomTypes.getRoomName(), hotelId);

            if (totalRoom != null && totalRoom > 0 && totalRoom > availableRooms.size()) {
                return ResponseClass.responseSuccess("Not enough rooms available");
            }
            double roomFarePerDay = roomTypes.getRoomFare();
            double totalRoomFare = roomFarePerDay * totalRoom * numberOfDays;

            List<Map<String, Object>> roomDetails = new ArrayList<>();
            for (Room room : availableRooms) {
                Map<String, Object> roomDetail = new HashMap<>();
                roomDetail.put("roomId", room.getRoomId());
                roomDetail.put("roomNo", room.getRoomNo());
                roomDetail.put("roomType", room.getRoomType());
                roomDetail.put("status", room.getStatus());
                roomDetail.put("hotelId", room.getHotelId());
                roomDetail.put("availableStatus", room.getAvailableStatus());
                roomDetail.put("farePerDay", roomFarePerDay);
                roomDetail.put("noOfDays", numberOfDays);
                roomDetail.put("totalFareRoom", roomFarePerDay * numberOfDays);
                roomDetails.add(roomDetail);
            }

            Map<String,Object>  map = new HashMap<>();
            map.put("available",roomDetails);
            map.put("totalFare",totalRoomFare);
            map.put("numberOfDays", numberOfDays);
            map.put("status", "success");
            return ResponseEntity.ok(map);
        } catch (Exception e) {
            return ResponseClass.internalServer("something went wrong");
        }
    }



    public ResponseEntity<?> roomBook(String token, String bookingType,List<Integer> roomNumbers, LocalDateTime checkInDate, LocalDateTime checkOutDate, String guestName, String phone, String email, Integer adult, Integer children, String address, Double totalPaid) {
        String hotelId =  configClass.tokenValue(token,"hotelId");
        String roleType = configClass.tokenValue(token, "roleType"); // Extracting ActionBy from token
        Hotel hotels = hotelRepo.findByStHotelId(hotelId);
        if(hotels == null)
        {
            return ResponseClass.responseFailure("hotel id is wrong");

        }

        Booking booking = new Booking();
        Guest guest = null;

        if ("ExistingGuest".equalsIgnoreCase(bookingType)) {
            Booking existingGuestBooking = bookingRepo.findByGuestEmailAndHotelId(email, hotelId);
            if (existingGuestBooking == null) {
                return ResponseClass.responseFailure("Guest not found for the given email");
            }
            Set<Guest> guests = guestRepo.findByEmailAndHotelId(email,hotelId);
            if (guests.isEmpty()) {
                return ResponseClass.responseFailure("Guest not found for the given email");
            }
            boolean banOrUnban = false;
            List<Guest> guest11 = guestRepo.findByHotelIdAndEmail(hotelId, email);
            for (Guest guest1 : guest11) {
                if (guest1.getIsUserBanned()) {
                    return ResponseClass.responseFailure("Guest is Banned and cannot Book room");
                }
            }
            for (Guest existingGuest : guests) {
                guest  = new Guest();
                guest.setFirstName(existingGuest.getFirstName());
                guest.setEmail(existingGuest.getEmail());
                guest.setHotelId(existingGuest.getHotelId());
                guest.setAddress(existingGuest.getAddress());
                guest.setPhone(existingGuest.getPhone());
                guest.setCheckInDate(checkInDate);
                guest.setIsActive(true);
                guestRepo.save(guest);
            }

            booking.setGuestName(existingGuestBooking.getGuestName());
            booking.setPhoneNo(existingGuestBooking.getPhoneNo());
            booking.setAddress(existingGuestBooking.getAddress());
        } else if ("WalkInGuest".equalsIgnoreCase(bookingType)) {
            Set<Guest> newUser = guestRepo.findByEmailAndHotelId(email, hotelId);
            if (!newUser.isEmpty()) {
                return ResponseClass.responseFailure("Guest email is already used!");
            }
            guest = new Guest();
            guest.setFirstName(guestName);
            guest.setEmail(email);
            guest.setHotelId(hotelId);
            guest.setAddress(address);
            guest.setPhone(phone);
            guest.setCheckInDate(checkInDate);
            guest.setIsActive(true);
            booking.setGuestName(guestName);
            booking.setPhoneNo(phone);
            booking.setGuestEmail(email);
            booking.setAddress(address);
            booking.setGuestName(guestName);

        } else {
            return ResponseClass.responseFailure("Invalid booking type");
        }
        String bookingNo = ConfigClass.idCreate(hotelId,"B");
        booking.setBookingNo(bookingNo);
        booking.setBookingType(bookingType);
        booking.setCheckInDate(checkInDate);
        booking.setCheckOutDate(checkOutDate);
        if(adult!=null)
        {
            booking.setAdults(adult);
        }
        if(children!=null)
        {
            booking.setChildren(children);
        }
        booking.setBookingStatus(true);
        booking.setHotelId(hotelId);

        List<Room> selectedRooms = new ArrayList<>();
        double totalAmount = 0;
        for (Integer roomNumber : roomNumbers) {
            Room room = roomRepo.findByHotelIdAndRoomNo(hotelId,roomNumber);
            if (room == null ) {
                return ResponseClass.responseFailure("room no is not correct");
            }

            RoomTypes roomTypes = roomTypesRepo.findByRoomNameContainingIgnoreCase(room.getRoomType());
            long daysBetween = Duration.between(checkInDate, checkOutDate).toDays();
            if (daysBetween <= 0) {
                daysBetween = 1;
            }
            double roomPrice = roomTypes.getRoomFare() * daysBetween;
            booking.setRoomFare(roomPrice);
            totalAmount += roomPrice;
            selectedRooms.add(room);

        }

        int noOfRooms = selectedRooms.size();
        totalAmount *=noOfRooms;
        double pendingAmount = totalAmount;
        if (totalPaid != null && totalPaid > 0.0) {
            pendingAmount = totalAmount - totalPaid;
            booking.setTotalPaid(totalPaid);
        }

        booking.setPendingAmount(pendingAmount);
        booking.setTotalRoom(noOfRooms);

        for (Room room : selectedRooms) {
            room.setAvailableStatus(false);
            roomRepo.save(room);
        }
        roomRepo.saveAll(selectedRooms);

        booking.setRoomNo(roomNumbers);
        booking.setTotalAmount(totalAmount);
        if (guest != null) {
            guest.setBookingId(booking.getBookingNo());
            guestRepo.save(guest);
        }

        //we need to add method to send the guest an email when ssuccessfully room booked and then add the email
        //notification history so that Room booked also adds up in notification history .

        String transactionNo = generateUniqueId();


        PaymentClass paymentClass = new PaymentClass();

        paymentClass.setBookingId(booking.getBookingId());
        List<PaymentClass> test =paymentRepo.findByTransactionNo(transactionNo);
        if(test==null){
            paymentClass.setTransactionNo(transactionNo);

        }
        paymentClass.setHotelId(hotelId);
        paymentClass.setBookingNo(bookingNo);
        paymentClass.setTotalAmount(totalAmount);
        paymentClass.setTransactionNo(transactionNo);
        paymentClass.setTotalPaid(totalPaid);
        paymentClass.setPendingAmount(pendingAmount);
        paymentClass.setRefundAmount(0.0);
        paymentClass.setRecievedAmount(0.0);
        paymentClass.setUserName(guestName);

        paymentClass.setUserEmail(email);
        paymentClass.setPaymentDate(LocalDateTime.now());
        paymentClass.setPaymentType("CASH");
        if(pendingAmount==0)
        {
            paymentClass.setPaymentStatus(PaymentStatus.SUCCESSFUL);
        }
        paymentClass.setPaymentStatus(PaymentStatus.PENDING);
        paymentRepo.save(paymentClass);
        bookingRepo.save(booking);
        bookingActionService.saveBookingAction(booking.getBookingNo(), roleType, LocalDateTime.now());
        receivedPaymentsService.processPayment("paymentReceived", booking.getBookingNo(), guestName, email, totalPaid, roleType ,totalAmount,pendingAmount);
        return ResponseClass.responseSuccess("booking created successfully");

    }
//    public static String isUnqiue(String transactionNo){
//
//    }
    public static String generateUniqueId() {
        Random random = new Random();
        int randomNumber = 100000 + random.nextInt(900000); // Generates a 6-digit number (100000 - 999999)
        return "CASH" + randomNumber;
    }

    public ResponseEntity<?> updateCheckOut(String token, Long bookingId) {
        String hotelId =  configClass.tokenValue(token,"hotelId");
        Booking booking = bookingRepo.findByHotelIdAndBookingId(hotelId, bookingId);
        if (booking == null) {
            return ResponseClass.responseFailure("Booking not found");
        }

        booking.setCheckOutDate(LocalDateTime.now());
        booking.setBookingStatus(false);
        List<Integer> roomNumbers = booking.getRoomNo();
        for (Integer roomNumber : roomNumbers) {
            Room room = roomRepo.findByHotelIdAndRoomNo(hotelId, roomNumber);
            if (room != null) {
                room.setAvailableStatus(true);
//                room.setCheckInDate(null);
//                room.setCheckOutDate(null);
                roomRepo.save(room);
            }
        }

        bookingRepo.save(booking);
        return ResponseClass.responseSuccess("Check out updated successfully");
    }

    public ResponseEntity<?> getBookings(String token) {
        String hotelId =  configClass.tokenValue(token,"hotelId");
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDate todayDate = currentDate.toLocalDate();
        List<Booking> todayBookings = bookingRepo.findByCheckInDateBetweenAndHotelId(todayDate.atStartOfDay(), todayDate.atTime(23, 59, 59),hotelId);
        int todayBooking = todayBookings.size();
        Map<String, Object> response = new HashMap<>();
        response.put("status","success");
        response.put("message","Get today bookings data");
        response.put("totalBooking", todayBooking);
        response.put("todayBooking",todayBookings);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getTodayBookedRoomNumbers(String token) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDate todayDate = currentDate.toLocalDate();

        List<Booking> todayBookings = bookingRepo.findByCheckInDateBetweenAndHotelId(todayDate.atStartOfDay(), todayDate.atTime(23, 59, 59), hotelId);

        List<Map<String, Object>> roomDetailsList = new ArrayList<>();

        for (Booking booking : todayBookings) {
            List<Integer> roomNumbersArray = booking.getRoomNo();
            for (Integer roomNumber : roomNumbersArray) {
                Room room = roomRepo.findByHotelIdAndRoomNo(hotelId, roomNumber);
                if (room != null) {
                    Map<String, Object> roomDetailMap = new HashMap<>();
                    roomDetailMap.put("room", room.getRoomNo());
                    roomDetailMap.put("roomType", room.getRoomType());
                    roomDetailsList.add(roomDetailMap);
                }
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Total today booked rooms");
        response.put("totalBookedRoomsToday", roomDetailsList.size());
        response.put("roomDetails", roomDetailsList);
        response.put("status", "success");

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getTodayCheckoutList(String token) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        LocalDateTime currentDate = LocalDateTime.now();
        LocalDate todayDate = currentDate.toLocalDate();
        List<Booking> todayCheckouts = bookingRepo.findByCheckOutDateBetweenAndHotelId(todayDate.atStartOfDay(), todayDate.atTime(23, 59, 59), hotelId);
        List<Map<String, Object>> checkoutDetails = new ArrayList<>();
        for (Booking booking : todayCheckouts) {
            Map<String, Object> checkoutDetail = new HashMap<>();
            checkoutDetail.put("guestName", booking.getGuestName());
            checkoutDetail.put("roomNumbers", booking.getRoomNo());
            checkoutDetail.put("checkOutDate", booking.getCheckOutDate());
            checkoutDetails.add(checkoutDetail);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalCheckoutsToday", todayCheckouts.size());
        response.put("todayCheckoutList", checkoutDetails);

        return ResponseEntity.ok(response);
    }




    public ResponseEntity<?> getCheckoutBookings(String token) {
        String hotelId = configClass.tokenValue(token, "hotelId");

        // Fetch bookings that have checked out (i.e., checkout date is before or equals the current date) and belong to the specified hotel
        LocalDateTime currentDate = LocalDateTime.now();
        List<Booking> checkoutBookings = bookingRepo.findByHotelIdAndCheckOutDateBeforeAndBookingStatus(hotelId, currentDate, false);

        // Collect the checkout booking details
        List<Map<String, Object>> checkoutBookingDetails = new ArrayList<>();
        for (Booking booking : checkoutBookings) {
            Map<String, Object> checkoutBookingDetail = new HashMap<>();
            checkoutBookingDetail.put("guestName", booking.getGuestName());
            checkoutBookingDetail.put("roomNumbers", booking.getRoomNo());
            checkoutBookingDetail.put("checkInDate", booking.getCheckInDate());
            checkoutBookingDetail.put("checkOutDate", booking.getCheckOutDate());
            checkoutBookingDetails.add(checkoutBookingDetail);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalCheckoutBookings", checkoutBookings.size());
        response.put("checkoutBookings", checkoutBookingDetails);

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<?> getAllBookings(String token, LocalDate startDate,LocalDate endDate) {
        String hotelId = configClass.tokenValue(token, "hotelId");

        List<Booking> allBookings = bookingRepo.findByHotelId(hotelId);
        if(startDate!=null&&endDate!=null)
        {
            allBookings = allBookings.stream()
                    .filter(b -> {
                        LocalDate checkInDate = b.getCheckInDate().toLocalDate();  // Extract date only
                        LocalDate checkOutDate = b.getCheckOutDate().toLocalDate(); // Extract date only
                        return !checkInDate.isBefore(startDate) && !checkOutDate.isAfter(endDate);
                    })
                    .collect(Collectors.toList());
        }
        return ResponseClass.responseSuccess("All Booking ","bookings", allBookings);
    }

    public ResponseEntity<?> addPremiumBooking(String token, LocalDate date, int roomNo, List<PremiumDTO> premiumServiceList) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        Booking booking = bookingRepo.findByRoomNoAndBookingStatus(roomNo, true);
        if (booking == null) {
            return ResponseClass.responseFailure("Booking not found");
        }

        Room room = roomRepo.findByHotelIdAndRoomNoAndStatus(hotelId, roomNo, true);
        if (room == null) {
            return ResponseClass.responseFailure("Room not available");
        }

//        System.out.println(booking.getBookingNo());
        for (PremiumDTO premiumDTO : premiumServiceList) {
            BookingPremium bookingPremium = new BookingPremium();
            bookingPremium.setBookingNo(booking.getBookingNo());
            bookingPremium.setHotelId(hotelId);
            bookingPremium.setBookingId(booking.getBookingId());
            bookingPremium.setRoomNo(roomNo);
            bookingPremium.setLocalDate(date);
            bookingPremium.setPremiumServiceList(List.of(premiumDTO.getPremiumServiceName()));
            bookingPremium.setQuantity(List.of(premiumDTO.getQuantity()));
            bookingPremiumRepo.save(bookingPremium);
        }


        return ResponseClass.responseSuccess("Premium added successfully");

    }

    public ResponseEntity<?> getAllAvailableRoom(String token) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<Room> allRooms = roomRepo.findByHotelIdAndAvailableStatus(hotelId, true);
        return ResponseClass.responseSuccess("All Available Rooms ","rooms", allRooms);
    }

    public ResponseEntity<?> getPremiumBookingId(String token, String bookingId) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<BookingPremium> bookingPremium = bookingPremiumRepo.findByHotelIdAndBookingNo(hotelId, bookingId);
        return ResponseClass.responseSuccess("Premium Booking","premiumBooking", bookingPremium);
    }

    public ResponseEntity<?> deleteById(String token, long bookingId) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        Booking booking = bookingRepo.findByBookingId(bookingId);
        if (booking == null) {
            return ResponseClass.responseFailure("Booking not found");
        }
        bookingRepo.delete(booking);
        return ResponseClass.responseSuccess("Booking deleted successfully");
    }

    public ResponseEntity<?> getDelayedCheckOut(String token, LocalDate startDate, LocalDate endDate) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<Booking> delayedBookings = bookingRepo.findByHotelIdAndBookingStatusAndCheckOutDateBefore(hotelId, false,LocalDateTime.now());
        if(startDate!=null&&endDate!=null)
        {
            delayedBookings = delayedBookings.stream()
                    .filter(b -> {
                        LocalDate checkInDate = b.getCheckInDate().toLocalDate();  // Extract date only
                        LocalDate checkOutDate = b.getCheckOutDate().toLocalDate(); // Extract date only
                        return !checkInDate.isBefore(startDate) && !checkOutDate.isAfter(endDate);
                    })
                    .collect(Collectors.toList());
        }

        return ResponseClass.responseSuccess("List of delayedBookings ","delayedCheckOut",delayedBookings);
    }

    public ResponseEntity<?> getUpComingCheckOut(String token) {
       String hotelId = configClass.tokenValue(token,"hotelId");
        List<Booking> upComingCheckOut = bookingRepo.findByHotelIdAndBookingStatusAndCheckOutDateAfter(hotelId, false,LocalDateTime.now());
        return ResponseClass.responseSuccess("List of upcoming bookings ","upcomingCheckOut", upComingCheckOut);
    }

    public ResponseEntity<?> getBookedRoomInAllBooking(String token) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<Room> allRooms = roomRepo.findByHotelIdAndStatus(hotelId, true);
        List<Room> bookedRooms = new ArrayList<>();
        for (Room room : allRooms) {
            int count = bookingRepo.countByRoomNoAndBookingStatus(room.getRoomNo(), true);
            if (count > 0) {
                bookedRooms.add(room);
            }
        }
        return ResponseClass.responseSuccess("List of booked rooms in all bookings ","bookedRooms", bookedRooms);
    }
    public ResponseEntity<?> getBookedRoomByBookingId(String token, long bookingId) {
        String hotelId = configClass.tokenValue(token, "hotelId");

        Booking booking = bookingRepo.findByBookingIdAndHotelId(bookingId, hotelId);
        if (booking == null) {
            return ResponseClass.responseFailure("No booking found for the given booking ID");
        }
        List<Room> bookedRooms = new ArrayList<>();
        List<Integer> roomNumbers = booking.getRoomNo();

        for (Integer roomNumber : roomNumbers) {
            Room room = roomRepo.findByHotelIdAndRoomNo(hotelId, roomNumber);
            if (room != null) {
                bookedRooms.add(room);
            }
        }

        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("bookingId", booking.getBookingId());
        bookingDetails.put("hotelId", booking.getHotelId());
        bookingDetails.put("checkInDate", booking.getCheckInDate());
        bookingDetails.put("checkOutDate", booking.getCheckOutDate());
        bookingDetails.put("guestName", booking.getGuestName()); // Example field
        bookingDetails.put("rooms", bookedRooms);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Booked rooms for the specified booking ID");
        response.put("data", bookingDetails);

        return ResponseEntity.ok(response);
    }

//    public ResponseEntity<?> cancelBooking(String token, String bookingId) {
//        String hotelId = configClass.tokenValue(token, "hotelId");
//        Booking booking = bookingRepo.findByBookingNoAndHotelId(bookingId,hotelId);
//        if (booking == null) {
//            return ResponseClass.responseFailure("Booking not found");
//        }
//        booking.setBookingCancel(true);
//        booking.setRefundable(true);
//        bookingRepo.save(booking);
//        return ResponseClass.responseSuccess("Booking cancelled successfully");
//    }

    public ResponseEntity<?> getCancelBooking(String token, LocalDate startDate, LocalDate endDate) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<Booking> cancelBookings = bookingRepo.findByHotelIdAndBookingCancel(hotelId, true);
        if(startDate!=null&&endDate!=null)
        {
            cancelBookings = cancelBookings.stream()
                    .filter(b -> {
                        LocalDate checkInDate = b.getCheckInDate().toLocalDate();  // Extract date only
                        LocalDate checkOutDate = b.getCheckOutDate().toLocalDate(); // Extract date only
                        return !checkInDate.isBefore(startDate) && !checkOutDate.isAfter(endDate);
                    })
                    .collect(Collectors.toList());
        }

        return ResponseClass.responseSuccess("List of canceled bookings ","cancelBookings", cancelBookings);
    }

    public ResponseEntity<?> getByBookingNo(String token, String bookingNo) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        Booking booking = bookingRepo.findByBookingNoAndHotelId(bookingNo, hotelId);
        if (booking == null) {
            return ResponseClass.responseFailure("Booking not found");
        }
        return ResponseClass.responseSuccess("Booking details","booking", booking);
    }

    public ResponseEntity<?> getByBookingId(String token, long bookingId) {
    String hotelId = configClass.tokenValue(token, "hotelId");
    Booking booking = bookingRepo.findByBookingIdAndHotelId(bookingId, hotelId);
    if (booking == null) {
        return ResponseClass.responseFailure("Booking not found");
    }
    return ResponseClass.responseSuccess("Booking details","booking", booking);
    }
    public ResponseEntity<?> getBookingDataByBookingId(String token  , long bookingId){
        String hotelId = configClass.tokenValue(token, "hotelId");
        Booking booking = bookingRepo.findByBookingIdAndHotelId(bookingId, hotelId);
        if (booking == null) {
            return ResponseClass.responseFailure("Booking not found");
        }
        List<Map<String, Object>> roomsWithDetails = new ArrayList<>();
        List<Integer> roomNumbers = booking.getRoomNo();

        for (Integer roomNumber : roomNumbers) {
            Room room = roomRepo.findByHotelIdAndRoomNo(hotelId, roomNumber);
            if (room != null) {
                Map<String, Object> roomDetails = new HashMap<>();
                roomDetails.put("roomNo", room.getRoomNo());
                roomDetails.put("roomType", room.getRoomType());
                roomsWithDetails.add(roomDetails);
            }
        }
        Map<String, Object> bookingDetails = new HashMap<>();
        bookingDetails.put("bookingId", booking.getBookingId());
        bookingDetails.put("hotelId", booking.getHotelId());
        bookingDetails.put("bookingNo", booking.getBookingNo());
        bookingDetails.put("bookingType", booking.getBookingType());
        bookingDetails.put("guestName", booking.getGuestName());
        bookingDetails.put("guestEmail", booking.getGuestEmail());
        bookingDetails.put("fare", booking.getTotalAmount());
        bookingDetails.put("refundable", booking.isRefundable());
        bookingDetails.put("roomNo", roomsWithDetails);
        bookingDetails.put("cancellationFee", 0.0);

        return ResponseClass.responseSuccess("Booking details retrieved successfully", "booking", bookingDetails);
    }

    public ResponseEntity<?> getAllCheckOut(String token, LocalDate startDate, LocalDate endDate) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<Booking> allCheckOut = bookingRepo.findByHotelIdAndBookingStatus(hotelId, false);
        if(startDate!=null&&endDate!=null)
        {
            allCheckOut = allCheckOut.stream()
                    .filter(b -> {
                        LocalDate checkInDate = b.getCheckInDate().toLocalDate();  // Extract date only
                        LocalDate checkOutDate = b.getCheckOutDate().toLocalDate(); // Extract date only
                        return !checkInDate.isBefore(startDate) && !checkOutDate.isAfter(endDate);
                    })
                    .collect(Collectors.toList());
        }
        return ResponseClass.responseSuccess("List of all check-out bookings ","allCheckOut", allCheckOut);
    }

    public ResponseEntity<?> getAllRefundable(String token, LocalDate startDate, LocalDate endDate) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<Booking> allRefundable = bookingRepo.findByHotelIdAndRefundable(hotelId, true);
        if(startDate!=null&&endDate!=null)
        {
            allRefundable = allRefundable.stream()
                    .filter(b -> {
                        LocalDate checkInDate = b.getCheckInDate().toLocalDate();  // Extract date only
                        LocalDate checkOutDate = b.getCheckOutDate().toLocalDate(); // Extract date only
                        return !checkInDate.isBefore(startDate) && !checkOutDate.isAfter(endDate);
                    })
                    .collect(Collectors.toList());
        }
        return ResponseClass.responseSuccess("List of all refundable bookings ","allRefundable", allRefundable);
    }

    public ResponseEntity<?> getTodayCheckOut(String token) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        LocalDateTime todayAtNoon = LocalDate.now().atTime(12, 0);
        List<Booking> todayCheckOut = bookingRepo.findByHotelIdAndBookingStatusAndCheckOutDate(hotelId, false, todayAtNoon);
        return ResponseClass.responseSuccess("List of today's check-out bookings ","todayCheckOut", todayCheckOut);
    }

    public ResponseEntity<?> getAllPremBooking(String token) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<BookingPremium> allPremBookings = bookingPremiumRepo.findByHotelId(hotelId);
        return ResponseClass.responseSuccess("List of all premium bookings ","allPremBookings", allPremBookings);
    }

    public ResponseEntity<?> getPendingCheckIn(String token) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<Booking> pendingCheckIn = bookingRepo.findByHotelIdAndBookingStatusAndCheckInDateBefore(hotelId, true, LocalDateTime.now());
        return ResponseClass.responseSuccess("List of pending check-in bookings ","pendingCheckIn", pendingCheckIn);
    }

    public ResponseEntity<?> getUpcomingCheckIn(String token) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<Booking> upcomingCheckIn = bookingRepo.findByHotelIdAndBookingStatusAndCheckInDateAfter(hotelId, true, LocalDateTime.now());
        return ResponseClass.responseSuccess("List of upcoming check-in bookings ","upcomingCheckIn", upcomingCheckIn);
    }

    public ResponseEntity<?> getPremBookingById(String token, long bookingId) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        List<BookingPremium> allPremBookings = bookingPremiumRepo.findByBookingId(bookingId);
        return ResponseClass.responseSuccess("List of all premium bookings ","allPremBookings", allPremBookings);
    }

      public ResponseEntity<?> bookingCancelById(String token, long bookingId) {
        String hotelId = configClass.tokenValue(token, "hotelId");
          String roleType = configClass.tokenValue(token, "roleType");
        Booking booking = bookingRepo.findByBookingIdAndHotelId(bookingId, hotelId);
        if (booking == null) {
            return ResponseClass.responseFailure("Booking not found");
        }
        booking.setBookingCancel(true);
        booking.setRefundable(true);
        bookingRepo.save(booking);
          returnedPaymentsService.addReturnedPayment(
                  booking.getBookingNo(),
                  booking.getGuestName(),
                  booking.getGuestEmail(),
                  booking.getTotalPaid(),
                  booking.getTotalAmount(),
                  booking.getPendingAmount(),
                 roleType
          );
        return ResponseClass.responseSuccess("Booking cancelled successfully");
    }

    public ResponseEntity<?> getCancelBookingById(String token, long bookingId) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        Booking booking = bookingRepo.findByBookingIdAndBookingCancel(bookingId, true);
        if (booking == null) {
            return ResponseClass.responseFailure("Booking not found");
        }
        return ResponseClass.responseSuccess("Booking cancel get","cancelBooking",booking) ;
    }


    public ResponseEntity<?> getActiveBookings(String token, LocalDate startDate, LocalDate endDate) {

        String hotelId = configClass.tokenValue(token, "hotelId");

        List<Booking> activeBookings = bookingRepo.findByHotelIdAndBookingStatus(hotelId, true);
        if(startDate!=null&&endDate!=null)
        {
            activeBookings = activeBookings.stream()
                    .filter(b -> {
                        LocalDate checkInDate = b.getCheckInDate().toLocalDate();  // Extract date only
                        LocalDate checkOutDate = b.getCheckOutDate().toLocalDate(); // Extract date only
                        return !checkInDate.isBefore(startDate) && !checkOutDate.isAfter(endDate);
                    })
                    .collect(Collectors.toList());
        }
        return ResponseClass.responseSuccess("All Active Booking","active",activeBookings);
    }
}
