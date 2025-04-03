package com.example.main.Hotel.Service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.Hotel.Entity.Admin;
import com.example.main.Hotel.Entity.Hotel;
import com.example.main.Hotel.Repo.AdminRepo;
import com.example.main.Hotel.Repo.HotelRepo;
import com.example.main.Login.Entity.LoginModel;
import com.example.main.Login.Repo.LoginRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HotelService {

    private final HotelRepo hotelsRepo;
    private final AdminRepo adminRepo;
    private final LoginRepo loginRepo;
    private final PasswordEncoder passwordEncoder;
    private final ConfigClass configClass;


    public ResponseEntity<?> addHotel(String hotelName, String subTitle, String destination, boolean status, LocalDate registerDate, String hotelClass, String phoneNo, String address, String hotelEmail, String description, String hotelId,MultipartFile[] file,String adminName, String adminEmail, String adminPassword, String adminAddress, String adminPhone) {

        Hotel hotelExits = hotelsRepo.findByHotelName(hotelName);
        if (hotelExits != null) {
            return ResponseClass.responseFailure("hotel name already exits");
        }
        Admin adminExist = adminRepo.findByAdminEmail(hotelEmail);
        if(adminExist != null){
            return ResponseClass.responseFailure("hotel email already exits in admin table");
        }
        LoginModel adminLoginExist = loginRepo.findByEmail(hotelEmail);
        if(adminLoginExist != null){
            return ResponseClass.responseFailure("hotel email already exits in login table");
        }

        Hotel hotels = new Hotel();
        String hotelId1 = ConfigClass.idCreate(hotelId);
        hotels.setHotelName(hotelName);
        hotels.setStHotelId(hotelId1);
        hotels.setStatus(true);
        hotels.setSubTitle(subTitle);
        hotels.setHotelClass(hotelClass);
        hotels.setAddress(address);
        hotels.setDestination(destination);
        hotels.setPhoneNo(phoneNo);
        hotels.setDescription(description);
        hotels.setHotelEmail(hotelEmail);
        if (registerDate == null) {
            registerDate = LocalDate.now();
        }
        hotels.setHotelDate(registerDate);
        hotels.setStatus(status);

        if (file != null && file.length > 0) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile files : file) {
                try {
                    String imagePath = ConfigClass.saveImage(files);
                    if (imagePath != null) {
                        imageUrls.add(imagePath);
                    }
                } catch (IOException e) {
                    return ResponseClass.responseFailure("Failed to store hotel image");
                }
            }
            hotels.setHotelImageUrl(imageUrls.toArray(new String[0]));
        }

        String adminPass;
        boolean isGeneratedPassword = false;
        if (adminPassword == null || adminPassword.isEmpty()) {
            adminPass = ConfigClass.generatePassword(10);
            isGeneratedPassword = true;
        } else {
            adminPass = adminPassword;
        }

        Admin admin = new Admin();
        admin.setAdminName(adminName);
        Admin adminExist1 = adminRepo.findByAdminEmail(adminEmail);
        if(adminExist1 != null){
            return ResponseClass.responseFailure("admin email already exits in admin table");
        }
        LoginModel adminLoginExist2 = loginRepo.findByEmail(adminEmail);
        if(adminLoginExist2 != null){
            return ResponseClass.responseFailure("admin email already exits in login table");

        }
        admin.setAdminEmail(adminEmail);
        admin.setAdminPassword(adminPass);
        admin.setAdminPassword(passwordEncoder.encode(adminPass));
        admin.setAdminPhoneNo(adminPhone);
        admin.setAdminAddress(adminAddress);
        admin.setRoleType("ADMIN");
        admin.setHotelId(hotelId1);
        adminRepo.save(admin);


        LoginModel adminLogin = new LoginModel();
        adminLogin.setRole("ADMIN");
        adminLogin.setUserName(adminName);
        adminLogin.setEmail(adminEmail);
        adminLogin.setPassword(passwordEncoder.encode(adminPass));
        adminLogin.setHotelId(hotelId1);
        loginRepo.save(adminLogin);
        hotelsRepo.save(hotels);
        if (isGeneratedPassword) {
            CompletableFuture<Boolean> emailSentFuture = configClass.sendEmail(
                    adminEmail,
                    "Your Admin Password",
                    "Your generated admin password is: " + adminPass
            );

            // Handle the CompletableFuture (optional: wait for it to complete)
            emailSentFuture.thenAccept(emailSent -> {
                if (!emailSent) {
                    log.warn("Failed to send the generated password to admin email: {}", adminEmail);
                }
            });

            return ResponseClass.responseSuccess("Hotel added successfully. The generated password has been sent to your email.");
        }

        return ResponseClass.responseSuccess("Hotel added successfully");
    }

    public ResponseEntity<?> updateHotel(long hotelId, String hotelName, String subTitle, String destination, Boolean status, LocalDate registerDate, String hotelClass, String phoneNo, String address, String hotelEmail, String description, MultipartFile[] hotelImage) {

        Hotel hotel = hotelsRepo.findByHotelId(hotelId);
        if (hotel == null) {
            return ResponseClass.responseFailure("hotel not found");
        }
        // Update hotel details if provided

        if (hotelName != null && !hotelName.isEmpty()) hotel.setHotelName(hotelName);
        if (subTitle != null) hotel.setSubTitle(subTitle);
        if (destination != null) hotel.setDestination(destination);
        if (status != null) hotel.setStatus(status);
        if (registerDate != null) hotel.setHotelDate(registerDate);
        if (hotelClass != null) hotel.setHotelClass(hotelClass);
        if (phoneNo != null) hotel.setPhoneNo(phoneNo);
        if (address != null) hotel.setAddress(address);
        if (hotelEmail != null) hotel.setHotelEmail(hotelEmail);
        if (description != null) hotel.setDescription(description);

        // Handle new image uploads if provided
        if (hotelImage != null && hotelImage.length > 0) {
            List<String> imageUrls = new ArrayList<>();
            for (MultipartFile file : hotelImage) {
                try {
                    String imagePath = ConfigClass.saveImage(file);
                    if (imagePath != null) {
                        imageUrls.add(imagePath);
                    }
                } catch (IOException e) {
                    return ResponseClass.responseFailure("Failed to store hotel image");
                }
            }
            // Replace the existing images with the new ones
            hotel.setHotelImageUrl(imageUrls.toArray(new String[0]));
        }

        hotelsRepo.save(hotel);
        return ResponseClass.responseSuccess("Hotel updated successfully");
    }

    public ResponseEntity<?> getHotelById(Long hotelId) {
        Hotel hotel = hotelsRepo.findByHotelId(hotelId);
        if (hotel == null) {
            return ResponseClass.responseFailure("hotel not found");
        }
        return ResponseEntity.ok(hotel);
    }

    public ResponseEntity<?> findByUnActiveHotel() {
        List<Hotel> hotels = hotelsRepo.findByStatus(false);
        if (hotels.isEmpty()) {
            return ResponseClass.responseSuccess("No UnActive hotels found");
        }
        return ResponseEntity.ok(hotels);
    }

    public ResponseEntity<?> getActiveHotel() {
        List<Hotel> hotels = hotelsRepo.findByStatus(true);
        if (hotels.isEmpty()) {
            return ResponseClass.responseSuccess("No Active hotels found");
        }
        return ResponseEntity.ok(hotels);
    }

    public ResponseEntity<?> deletebyId(Long hotelId) {
        Hotel hotel = hotelsRepo.findByHotelId(hotelId);
        if (hotel == null) {
            return ResponseClass.responseFailure("hotel not found");
        }
        hotelsRepo.delete(hotel);
        return ResponseClass.responseSuccess("hotel deleted successfully");
    }

    public ResponseEntity<?> getAllHotels() {
        List<Hotel> hotels = hotelsRepo.findAll();
        if (hotels.isEmpty()) {
            return ResponseClass.responseSuccess("No hotels found");
        }
        return ResponseEntity.ok(hotels);
    }
}
