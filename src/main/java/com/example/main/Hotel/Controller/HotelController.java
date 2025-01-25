package com.example.main.Hotel.Controller;

import com.example.main.Hotel.Service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/hotel")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class HotelController {

    private final HotelService hotelService;


    @PostMapping("/addHotel")
    public ResponseEntity<?> addHotel( @RequestParam String hotelName,
                                        @RequestParam String subTitle, @RequestParam String destination,
                                        @RequestParam boolean status, @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate registerDate, @RequestParam(required = false) String hotelClass,
                                        @RequestParam String phoneNo, @RequestParam String address, @RequestParam String hotelEmail, @RequestParam(required = false) String description,@RequestParam(required = false) String hotelId,
                                        @RequestParam(required = false) MultipartFile[] hotelImage,
                                       @RequestParam String adminName,
                                       @RequestParam String adminEmail,
                                       @RequestParam(required = false) String adminPassword,
                                       @RequestParam String adminAddress,
                                       @RequestParam String adminPhone)
                                        {

        return  hotelService.addHotel(hotelName, subTitle, destination, status,registerDate,hotelClass,phoneNo,address,hotelEmail,description,hotelId,hotelImage,adminName,adminEmail,adminPassword,adminAddress,adminPhone);
    }


    @PutMapping("/updateHotel/{hotelId}")
    public ResponseEntity<?> updateHotel(@PathVariable long hotelId,
                                         @RequestParam(required = false) String hotelName,
                                         @RequestParam(required = false) String subTitle,
                                         @RequestParam(required = false) String destination,
                                         @RequestParam(required = false) boolean status,
                                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDate registerDate,
                                         @RequestParam(required = false) String hotelClass,
                                         @RequestParam(required = false) String phoneNo,
                                         @RequestParam(required = false) String address,
                                         @RequestParam(required = false) String hotelEmail,
                                         @RequestParam(required = false) String description,
                                         @RequestParam(required = false) MultipartFile[] hotelImage) {

        return hotelService.updateHotel(hotelId,hotelName,subTitle,destination,status,registerDate,hotelClass,phoneNo,address,hotelEmail,description,hotelImage);

    }

    @GetMapping("/getHotelById/{hotelId}")
    public ResponseEntity<?> getHotelById(@PathVariable("hotelId") Long hotelId) {
        return hotelService.getHotelById(hotelId);
    }


    @GetMapping("/getUnActiveHotels")
    public  ResponseEntity<?> findUnActiveHotel()
    {
        return hotelService.findByUnActiveHotel();
    }


    @GetMapping("/getActiveHotels")
    public  ResponseEntity<?> findActiveHotel()
    {
        return hotelService.getActiveHotel();
    }


    @DeleteMapping("/deleteByHotelId/{hotelId}")
    public ResponseEntity<?> deleteByHotelID(@PathVariable("hotelId") Long hotelId) {
        return hotelService.deletebyId(hotelId);
    }


    @GetMapping("/getAllHotels")
    public ResponseEntity<?> getAllHotels() {
        return hotelService.getAllHotels();
    }



}
