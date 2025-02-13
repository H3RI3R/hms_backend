package com.example.main.Searching.Service;

import com.example.main.Exception.ResponseClass;
import com.example.main.GuestManagement.Model.Guest;
import com.example.main.GuestManagement.Repository.GuestRepo;
import com.example.main.StaffManagement.Model.Role;
import com.example.main.StaffManagement.Model.Staff;
import com.example.main.StaffManagement.Repository.StaffRepo;
import com.example.main.Support.DateTimeWeekDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class SearchingService {
    @Autowired
    private StaffRepo staffRepo;

    @Autowired
    private GuestRepo guestRepo;

    public ResponseEntity<?> searchStaffByEmail(String hotelId, String email) {
        List<Staff> staffs = staffRepo.findByHotelIdAndEmail(hotelId, email);

        if (staffs.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Staff not found with email: " + email);
        }

        List<StaffDTORecord> staffs1 = staffs.stream().map(staff -> new StaffDTORecord(
                staff.getId(),
                staff.getHotelId(),
                staff.getName(),
                staff.getUserName(),
                staff.getEmail(),
                staff.getRole(),
                (staff.getStatus() == null ? "ENABLED" : staff.getStatus()),
                new DateTimeWeekDay(staff.getCreatedAt() == null ? Instant.now() : staff.getCreatedAt()))).toList();

        return ResponseClass.responseSuccess("Staff found", "staffs", staffs1);
    }

    record StaffDTORecord(
            Long id,
            String hotelId,
            String name,
            String userName,
            String email,
            Role role,
            String status,
            DateTimeWeekDay createdAt
    ) {
    }

    public ResponseEntity<?> searchGuestByEmail(String hotelId, String email) {
        List<Guest> guests = guestRepo.findByHotelIdAndEmail(hotelId, email);

        if (guests.isEmpty()) {
            return ResponseClass.responseFailure("Guest not found with email: " + email);
        }

        List<GuestDTORecord> guestDtoList = guests.stream().map(GuestDTORecord::new).toList();
        return ResponseClass.responseSuccess("Guest information", "guest", guestDtoList);
    }

    public record GuestDTORecord(
            Long id,
            String firstName,
            String lastName,
            String hotelId,
            String email,
            String countryCode,
            String phone,
            Boolean isPhoneVerified,
            Boolean isEmailVerified,
            String address,
            String city,
            String state,
            Integer zipCode,
            String country,
            String bookingId,
            String checkInDate,
            Boolean isUserBanned,
            Boolean isDeleted,
            Boolean isActive,
            Boolean isSubscriber,
            DateTimeWeekDay createdAt,
            long dayAgo

    ) {
        public GuestDTORecord(Guest guest) {
            this(
                    guest.getId(),
                    guest.getFirstName(),
                    guest.getLastName(),
                    guest.getHotelId(),
                    guest.getEmail(),
                    guest.getCountryCode(),
                    guest.getPhone(),
                    guest.getIsPhoneVerified(),
                    guest.getIsEmailVerified(),
                    guest.getAddress(),
                    guest.getCity(),
                    guest.getState(),
                    guest.getZipCode(),
                    guest.getCountry(),
                    guest.getBookingId(),
                    (guest.getCheckInDate() != null) ? DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss").withZone(ZoneId.systemDefault()).format(guest.getCheckInDate()) : null,
                    guest.getIsUserBanned(),
                    guest.getIsDeleted(),
                    guest.getIsActive(),
                    guest.getIsSubscriber(),
                    new DateTimeWeekDay(guest.getCreatedAt() == null ? Instant.now() : guest.getCreatedAt()),
                    ChronoUnit.DAYS.between(guest.getCreatedAt(), Instant.now())
            );
        }
    }
}