package com.example.main.GuestManagement.Controller;

import com.example.main.Configuration.ConfigClass;
import com.example.main.GuestManagement.Service.GuestService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@ComponentScan
@RestController
@RequestMapping("/guest")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GuestController {
    private final ConfigClass configClass;
    private final GuestService guestService;


    @PostMapping("/create")
    public ResponseEntity<?> createPermission(
            @RequestHeader("Authorization") String token,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String countryCode,
            @RequestParam Boolean isPhoneVerified,
            @RequestParam Boolean isEmailVerified,
            @RequestParam Boolean isUserBanned,
            @RequestParam String address,
            @RequestParam String city,
            @RequestParam String state,
            @RequestParam Integer zipCode){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return guestService.createGuest(
                hotelId, firstName, lastName,
                email, phone, countryCode, isPhoneVerified,
                isEmailVerified, isUserBanned, address, city, state, zipCode);
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<?> createPermission(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String countryCode,
            @RequestParam(required = false) Boolean isPhoneVerified,
            @RequestParam(required = false) Boolean isEmailVerified,
            @RequestParam(required = false) Boolean isUserBanned,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) Integer zipCode,
            @RequestParam(required = false) Boolean banUser,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) Boolean isActive){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return guestService.updateGuestDetails(
                hotelId, id, firstName, lastName,
                email, phone, countryCode, isPhoneVerified,
                isEmailVerified, isUserBanned, address, city, state, zipCode, banUser, country, isActive);
    }

    @GetMapping("/getOne/{id}")
    public ResponseEntity<?> getOneGuest(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return guestService.getOneGuest(hotelId, id);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllGuest(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "search", required = false) String email) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return guestService.getAllGuest(hotelId, email);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteGuest(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return guestService.deleteGuest(hotelId, id);
    }


    @GetMapping("/active")
    public ResponseEntity<?> getAllActiveGuest(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "search", required = false) String email) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return guestService.getAllActiveGuest(hotelId, email);
    }


    @GetMapping("/banned")
    public ResponseEntity<?> getAllBannedGuest(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "search", required = false) String email) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return guestService.getAllBannedGuests(hotelId, email);
    }


    @GetMapping("/emailUnverified")
    public ResponseEntity<?> getAllEmailUnverified(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "search", required = false) String email) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return guestService.getAllEmailUnverifiedGuests(hotelId, email);
    }


    @GetMapping("/phoneUnverified")
    public ResponseEntity<?> getAllPhoneUnverified(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "search", required = false) String email) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        return guestService.getAllPhoneUnverifiedGuests(hotelId, email);
    }

    @GetMapping("/banGuest/{id}")
    public ResponseEntity<?> banGuest(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id){
        String hotelId = configClass.tokenValue(token, "hotelId");
        return guestService.banGuest(hotelId, id);
    }

}