package com.example.main.ManageHotel.Controller;

import com.example.main.ManageHotel.Service.FacilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/facilities")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FacilityController {

    private final FacilityService facilityService;

    @PostMapping("/add")
    ResponseEntity<?> createFacility(@RequestHeader("Authorization") String token,
                                     @RequestParam String facilityName,
                                     @RequestParam boolean status,
                                     @RequestParam(value = "icon") MultipartFile icon) {
        return facilityService.createFacility(token, facilityName, status, icon);
    }

    @PostMapping("/update/{id}")
    ResponseEntity<?> updateFacility(@RequestHeader("Authorization") String token,
                                     @PathVariable("id") long facilityId,
                                     @RequestParam(required = false) String facilityName,
                                     @RequestParam(required = false) Boolean status,
                                     @RequestParam(required = false) MultipartFile icon) {
        return facilityService.updateFacility(token, facilityId, facilityName, status, icon);
    }



    @GetMapping("/getAll")
    public ResponseEntity<?> getAllFacilities(@RequestHeader("Authorization") String token) {
        return facilityService.getAllFacilities(token);
    }

    @GetMapping("/getById/{facilityId}")
    public ResponseEntity<?> getFacilityById(@RequestHeader("Authorization") String token,
                                             @PathVariable("facilityId") long facilityId) {
        return facilityService.getFacilityById(token, facilityId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteFacilityById(@RequestHeader("Authorization") String token,
                                                @PathVariable("id") long facilityId) {
        return facilityService.deleteFacilityById(token, facilityId);
    }
}

