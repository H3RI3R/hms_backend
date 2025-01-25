package com.example.main.ManageHotel.Service;


import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.ManageHotel.Entity.BedTypes;
import com.example.main.ManageHotel.Entity.RoomTypes;
import com.example.main.ManageHotel.Repo.BedTypeRepo;
import com.example.main.ManageHotel.Repo.RoomTypesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BedTypeService {

    private final BedTypeRepo bedTypeRepo;
    private final ConfigClass config;
    private final RoomTypesRepo roomTypesRepo;

    public ResponseEntity<?> createBedType(String token, String bedName, boolean status, MultipartFile bedImage) {
        String hotelId = config.tokenValue(token, "hotelId");
        BedTypes bedType = new BedTypes();
        BedTypes bed = bedTypeRepo.findByBedNameContainingIgnoreCaseAndHotelId(bedName,hotelId);
        if (bed!= null) {
            return ResponseClass.responseFailure("Bed type already exists in this hotel");
        }
        bedType.setBedName(bedName);
        bedType.setStatus(status);
        bedType.setHotelId(hotelId);
        if(bedImage!=null && !bedImage.isEmpty())
        {
            try {
                String imagePath = ConfigClass.saveImage(bedImage);
                if (imagePath != null) {
                    bedType.setBedImage(imagePath);
                }
            } catch (IOException e) {
                return ResponseClass.responseFailure("Failed to store image");
            }
        }

        bedTypeRepo.save(bedType);
        return ResponseClass.responseSuccess("Bed type added successfully");
    }

    public ResponseEntity<?> updateBedType(String token, long bedTypeId, String bedName, Boolean status, MultipartFile bedImage) {
        String hotelId = config.tokenValue(token, "hotelId");
        BedTypes bedType = bedTypeRepo.findByBedTypeId(bedTypeId);
        if (bedType == null) {
            return ResponseClass.responseFailure("Bed type not found");
        }
        if (!bedType.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Bed type does not belong to the specified hotel");
        }
        BedTypes bed = bedTypeRepo.findByBedNameContainingIgnoreCaseAndHotelId(bedName,hotelId);
        if (bed!= null) {
            return ResponseClass.responseFailure("Bed type already exists in this hotel");
        }

        if (bedName != null) {
            bedType.setBedName(bedName);
        }
        if(status !=null)
        {
            bedType.setStatus(status);
        }

        if (bedImage != null && !bedImage.isEmpty()) {
            try {
                String imagePath = ConfigClass.saveImage(bedImage);
                if (imagePath != null) {
                    bedType.setBedImage(imagePath);
                }
            } catch (IOException e) {
                return ResponseClass.responseFailure("Failed to store image");
            }
        }

        bedTypeRepo.save(bedType);
        return ResponseClass.responseSuccess("Bed type updated successfully");
    }


    public ResponseEntity<?> getAllBedTypes(String token) {
        String hotelId = config.tokenValue(token, "hotelId");
        List<BedTypes> bedTypesList = bedTypeRepo.findByHotelId(hotelId);
    
        return ResponseClass.responseSuccess("Bed types retrieved successfully", "bedTypes", bedTypesList);
    }

    public ResponseEntity<?> getBedTypeById(String token, long bedTypeId) {
        String hotelId = config.tokenValue(token, "hotelId");
        BedTypes bedType = bedTypeRepo.findByBedTypeId(bedTypeId);
        if (bedType == null || !bedType.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Bed type not found or does not belong to the specified hotel");
        }
        return ResponseClass.responseSuccess("Bed type retrieved successfully", "bedType", bedType);
    }

    public ResponseEntity<?> deleteBedTypeById(String token, long bedTypeId) {
        String hotelId = config.tokenValue(token, "hotelId");
        BedTypes bedType = bedTypeRepo.findByBedTypeId(bedTypeId);
        if (bedType == null || !bedType.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Bed type not found or does not belong to the specified hotel");
        }
        List<RoomTypes> roomTypes = roomTypesRepo.findByBedTypes_BedTypeId(bedTypeId);
        ArrayList<String> bedTypes = new ArrayList<String>();
        if(roomTypes != null)
        {
            for(RoomTypes roomType : roomTypes)
            {
                bedTypes.add(roomType.getRoomName());
            }
            Map<String, Object> map = new HashMap<>();
            map.put("message", "Cannot delete this bed type because it is associated with room types");
            map.put("roomTypes", bedTypes);
            map.put("status", "false");
            return ResponseEntity.ok(map);
//            return ResponseClass.responseFailure("Cannot delete this bed type because it is associated with room types"+bedTypes);
        }
        bedTypeRepo.delete(bedType);
        return ResponseClass.responseSuccess("Bed type deleted successfully");
    }
}
