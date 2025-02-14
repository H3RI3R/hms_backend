package com.example.main.GuestManagement.Service;

import com.example.main.Exception.ResponseClass;
import com.example.main.GuestManagement.Model.Guest;
import com.example.main.GuestManagement.Repository.GuestRepo;
import com.example.main.Support.DateTimeWeekDay;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GuestService {
    private final GuestRepo guestRepo;

    public ResponseEntity<?> updateGuestDetails(String hotelId, Long id, String firstName, String lastName, String email, String phone, String countryCode, Boolean isPhoneVerified, Boolean isEmailVerified, Boolean isUserBanned, String address, String city, String state, Integer zipCode, Boolean banUser, String country, Boolean isActive) {
        Guest guest = guestRepo.findById(id).orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "guest does not exist"));
        if(!guest.getHotelId().equals(hotelId)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "invalid request");
        guest.setFirstName(firstName != null ? firstName : guest.getFirstName());
        guest.setLastName(lastName != null ? lastName : guest.getLastName());
        guest.setEmail(email != null ? email : guest.getEmail());
        guest.setPhone(phone != null ? phone : guest.getPhone());
        guest.setCountryCode(countryCode != null ? countryCode : guest.getCountryCode());
        guest.setIsPhoneVerified(isPhoneVerified != null ? isPhoneVerified : guest.getIsPhoneVerified());
        guest.setIsEmailVerified(isEmailVerified != null ? isEmailVerified : guest.getIsEmailVerified());
        guest.setIsUserBanned(isUserBanned != null ? isUserBanned : guest.getIsUserBanned());
        guest.setAddress(address != null ? address : guest.getAddress());
        guest.setCity(city != null ? city : guest.getCity());
        guest.setState(state != null ? state : guest.getState());
        guest.setZipCode(zipCode != null ? zipCode : guest.getZipCode());
        guest.setCountry(country != null ? country : guest.getCountry());
        guest.setIsActive(isActive != null ? isActive : guest.getIsActive());
        if(banUser !=null){
            if(banUser.equals(true) || banUser.equals(false)){
                guest.setIsUserBanned(banUser); } }
        guestRepo.save(guest);
        return ResponseClass.responseSuccess("guest information updated", "guest", guest);
    }

    public ResponseEntity<?> getOneGuest(String hotelId, Long id) {
        Guest guest = guestRepo.findByIdAndHotelId(id, hotelId);
        if (guest == null) {return ResponseClass.responseFailure("records not found");}
        GuestDTORecord guestDto = new GuestDTORecord(guest);
        return ResponseClass.responseSuccess("guest information", "guest", guestDto);
    }

    public ResponseEntity<?> getAllGuest(String hotelId, String email) {
        List<Guest> guestList;
        if (email != null && !email.isEmpty()) {
            guestList = guestRepo.findByHotelIdAndEmail(hotelId, email);
        } else {
            guestList = guestRepo.findAllByHotelId(hotelId);
        }

        if (guestList.isEmpty()) {
            return ResponseClass.responseFailure("records not found");
        }

        List<GuestDTORecord> guestDtoList = guestList.stream().map(GuestDTORecord::new).toList();
        return ResponseClass.responseSuccess("guest information", "guest", guestDtoList);
    }

    public ResponseEntity<?> deleteGuest(String hotelId, Long id) {
        Guest guest = guestRepo.findByIdAndHotelId(id, hotelId);
        if (guest == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "guest does not exist");
        guest.setIsDeleted(true);
        return ResponseClass.responseSuccess("guest deleted");
    }

    public ResponseEntity<?> createGuest(String hotelId, String firstName, String lastName, String email, String phone, String countryCode, Boolean isPhoneVerified, Boolean isEmailVerified, Boolean isUserBanned, String address, String city, String state, Integer zipCode) {
        Guest guest = new Guest();
        guest.setFirstName(firstName);
        guest.setLastName(lastName);
        guest.setEmail(email);
        guest.setPhone(phone);
        guest.setCountryCode(countryCode);
        guest.setIsPhoneVerified(isPhoneVerified);
        guest.setIsEmailVerified(isEmailVerified);
        guest.setIsUserBanned(isUserBanned);
        guest.setAddress(address);
        guest.setCity(city);
        guest.setState(state);
        guest.setZipCode(zipCode);
        guest.setHotelId(hotelId);
        guest.setCreatedAt(Instant.now());
        guestRepo.save(guest);
        return ResponseClass.responseSuccess("guest created", "guest", guest);
    }

    public ResponseEntity<?> getAllActiveGuest(String hotelId, String email) {
        List<Guest> guestList;
        if (email != null && !email.isEmpty()) {
            guestList = guestRepo.findByHotelIdAndEmail(hotelId, email).stream()
                    .filter(Guest::getIsActive)
                    .collect(Collectors.toList());
        } else {
            guestList = guestRepo.findAllByActiveHotelId(hotelId);
        }

        if (guestList.isEmpty()) {
            return ResponseClass.responseFailure("records not found");
        }

        List<GuestDTORecord> guestDtoList = guestList.stream().map(GuestDTORecord::new).toList();
        return ResponseClass.responseSuccess("guest information", "guest", guestDtoList);
    }

    public ResponseEntity<?> getAllBannedGuests(String hotelId, String email) {
        List<Guest> guestList;
        if (email != null && !email.isEmpty()) {
            guestList = guestRepo.findByHotelIdAndEmail(hotelId, email).stream()
                    .filter(Guest::getIsUserBanned)
                    .collect(Collectors.toList());
        } else {
            guestList = guestRepo.findAllByHotelIdAndIsUserBanned(hotelId, true);
        }

        if (guestList.isEmpty()) {
            return ResponseClass.responseFailure("records not found");
        }

        List<GuestDTORecord> guestDtoList = guestList.stream().map(GuestDTORecord::new).toList();
        return ResponseClass.responseSuccess("guest information", "guest", guestDtoList);
    }

    public ResponseEntity<?> getAllEmailUnverifiedGuests(String hotelId, String email) {
        List<Guest> guestList;
        if (email != null && !email.isEmpty()) {
            guestList = guestRepo.findByHotelIdAndEmail(hotelId, email).stream()
                    .filter(guest -> !guest.getIsEmailVerified())
                    .collect(Collectors.toList());
        } else {
            guestList = guestRepo.findAllByHotelIdAndIsEmailVerified(hotelId, false);
        }

        if (guestList.isEmpty()) {
            return ResponseClass.responseFailure("records not found");
        }

        List<GuestDTORecord> guestDtoList = guestList.stream().map(GuestDTORecord::new).toList();
        return ResponseClass.responseSuccess("guest information", "guest", guestDtoList);
    }

    public ResponseEntity<?> getAllPhoneUnverifiedGuests(String hotelId, String email) {
        List<Guest> guestList;
        if (email != null && !email.isEmpty()) {
            guestList = guestRepo.findByHotelIdAndEmail(hotelId, email).stream()
                    .filter(guest -> !guest.getIsPhoneVerified())
                    .collect(Collectors.toList());
        } else {
            guestList = guestRepo.findAllByHotelIdAndIsPhoneVerified(hotelId, false);
        }

        if (guestList.isEmpty()) {
            return ResponseClass.responseFailure("records not found");
        }

        List<GuestDTORecord> guestDtoList = guestList.stream().map(GuestDTORecord::new).toList();
        return ResponseClass.responseSuccess("guest information", "guest", guestDtoList);
    }

    public ResponseEntity<?> banGuest(String hotelId, Long id) {
        Guest guest = guestRepo.findByIdAndHotelId(id, hotelId);
        if (guest == null){
            return ResponseClass.responseFailure("guest not found");
        }
        if(guest.getIsDeleted()){
            return ResponseClass.responseFailure("guest not found");
        }
        if(guest.getIsUserBanned()!=null && guest.getIsUserBanned()){
            guest.setIsUserBanned(false);
            guestRepo.save(guest);
            return ResponseClass.responseSuccess("guest unbanned");
        }else {
            guest.setIsUserBanned(true);
            guestRepo.save(guest);
            return ResponseClass.responseSuccess("guest banned");
        }
    }


    record GuestDTORecord(
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
            LocalDateTime checkInDate,
            Boolean isUserBanned,
            Boolean isDeleted,
            Boolean isActive,
            Boolean isSubscriber,
            DateTimeWeekDay createdAt,
            Long dayAgo
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
                    guest.getCheckInDate(),
                    guest.getIsUserBanned(),
                    guest.getIsDeleted(),
                    guest.getIsActive(),
                    guest.getIsSubscriber(),
                    new DateTimeWeekDay(guest.getCreatedAt()),
                    Duration.between(Instant.now(), (guest.getCreatedAt() != null ? guest.getCreatedAt() : Instant.now())).toDays()
            );
        }
    }

}