package com.example.main.ManageHotel.Service;


import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.ManageHotel.Entity.Facility;
import com.example.main.ManageHotel.Repo.FacilityRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FacilityService {

    private final FacilityRepo facilityRepo;
    private final ConfigClass config;

    public ResponseEntity<?> createFacility(String token, String facilityName, boolean status, MultipartFile icon) {
        String roleType = config.tokenValue(token, "roleType");
        String hotelId = config.tokenValue(token, "hotelId");
        Facility facility1 = facilityRepo.findByFacilityNameContainingIgnoreCaseAndHotelId(facilityName, hotelId);
        if (facility1 != null) {
            return ResponseClass.responseFailure("Facility already  exists in this Hotel");
        }
        Facility facility = new Facility();
        facility.setFacilityName(facilityName);
        facility.setStatus(status);
        facility.setHotelId(hotelId);
        try {
            String imagePath = ConfigClass.saveImage(icon);
            if (imagePath != null) {
                facility.setFacilityImage(imagePath);
            }
        } catch (IOException e) {
            return ResponseClass.responseFailure("Failed to store image");
        }
        facilityRepo.save(facility);
        return ResponseClass.responseSuccess("Facility added successfully");
    }

    public ResponseEntity<?> updateFacility(String token, long facilityId, String facilityName, Boolean status, MultipartFile icon) {
        String hotelId = config.tokenValue(token, "hotelId");

        Facility facility = facilityRepo.findByFacilityId(facilityId);
        if (facility == null || !facility.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Facility not found or does not belong to the specified hotel");
        }
        Facility facility1 = facilityRepo.findByFacilityNameContainingIgnoreCaseAndHotelId(facilityName, hotelId);
        if (facility1 != null) {
            return ResponseClass.responseFailure("Facility already  exists in this Hotel");
        }

        if (facilityName != null) {
            facility.setFacilityName(facilityName);
        }

        if(status !=null)
        {
            facility.setStatus(status);
        }


        if (icon != null && !icon.isEmpty()) {
            try {
                String imagePath = ConfigClass.saveImage(icon);
                if (imagePath != null) {
                    facility.setFacilityImage(imagePath);
                }
            } catch (IOException e) {
                return ResponseClass.responseFailure("Failed to store image");
            }
        }

        facilityRepo.save(facility);

        return ResponseClass.responseSuccess("Facility updated successfully");
    }


    public ResponseEntity<?> getAllFacilities(String token) {
        String hotelId = config.tokenValue(token, "hotelId");

        List<Facility> facilityList = facilityRepo.findByHotelId(hotelId);
       

        return ResponseClass.responseSuccess("Facilities retrieved successfully", "Facilities", facilityList);
    }

    public ResponseEntity<?> getFacilityById(String token, long facilityId) {
        String hotelId = config.tokenValue(token, "hotelId");

        Facility facility = facilityRepo.findByFacilityId(facilityId);
        if (facility == null || !facility.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Facility not found or does not belong to the specified hotel");
        }
        return ResponseClass.responseSuccess("Facility retrieved successfully", "Facility", facility);
    }

    public ResponseEntity<?> deleteFacilityById(String token, long facilityId) {
        String hotelId = config.tokenValue(token, "hotelId");
        Facility facility = facilityRepo.findByFacilityId(facilityId);

        if (facility == null || !facility.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Facility not found or does not belong to the specified hotel");
        }
        facilityRepo.delete(facility);
        return ResponseClass.responseSuccess("Facility deleted successfully");
    }
}
