package com.example.main.ManageHotel.Controller;

import com.example.main.Configuration.ConfigClass;
import com.example.main.ManageHotel.Service.AmenitiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/amenites")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AmenitiesController {


    private final AmenitiesService amenitiesService;
    private final ConfigClass configClass;

    @PostMapping("/add")
    ResponseEntity<?> createAmenities(@RequestHeader("Authorization") String token
                                        , @RequestParam String amenitiesName , @RequestParam boolean status,
                                      @RequestParam(required = false) MultipartFile icon){
        String hotelId = configClass.tokenValue(token,"hotelId");
        return  amenitiesService.createAmenities(hotelId,amenitiesName,status,icon);

    }
    @PostMapping("/update/{id}")
    ResponseEntity<?> updateAmenities(@RequestHeader("Authorization") String token,@PathVariable("id") long amenitiesId,@RequestParam(required = false) String amenitiesName ,  @RequestParam(required = false) Boolean status, @RequestParam(required = false) MultipartFile files){

        return  amenitiesService.updateAmenities(token,amenitiesId,amenitiesName,status,files);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllAmenities(@RequestHeader("Authorization") String token) {
        return amenitiesService.getAllAmenities(token);
    }

    @GetMapping("/getById/{amenitiesId}")
    public ResponseEntity<?> getAmenitiesById(@RequestHeader("Authorization") String token,@PathVariable("amenitiesId") long amenitiesId) {

        return amenitiesService.getAmenitiesById(token,amenitiesId);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAmenitiesById(@RequestHeader("Authorization") String token, @PathVariable("id") long amenitiesId) {
        return amenitiesService.deleteAmenitiesById(token,amenitiesId);
    }

}
