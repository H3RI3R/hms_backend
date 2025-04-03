package com.example.main.ManageHotel.Service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.ManageHotel.DTO.RoomTypesDTO;
import com.example.main.ManageHotel.Entity.Amenities;
import com.example.main.ManageHotel.Entity.BedTypes;
import com.example.main.ManageHotel.Entity.RoomTypes;
import com.example.main.ManageHotel.Repo.AmenitiesRepo;
import com.example.main.ManageHotel.Repo.BedTypeRepo;
import com.example.main.ManageHotel.Repo.RoomTypesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RoomTypesService {

    private final RoomTypesRepo roomTypesRepo;

    private final ConfigClass config;
    private final AmenitiesRepo amenitiesRepo;
    private final BedTypeRepo bedTypesRepo;

//    public ResponseEntity<?> createRoomTypes(String token, RoomTypes roomTypes) {
//    String hotelId = config.tokenValue(token,"hotelId");
//    RoomTypes roomTypes1 = roomTypesRepo.findByRoomNameAndHotelId(roomTypes.getRoomName(), hotelId);
//    if (roomTypes1 != null) {
//        return ResponseClass.responseFailure("Room type with this name already exists in this hotel");
//    }
//    roomTypes.setHotelId(hotelId);
//    roomTypesRepo.save(roomTypes);
//    return ResponseClass.responseSuccess("Room Types saved successfully");
//    }

    public ResponseEntity<?> addRoomTypes(String token, RoomTypesDTO roomTypesDTO) {
        String hotelId = config.tokenValue(token, "hotelId");

        // Check if room type already exists in the hotel
        RoomTypes existingRoomTypes = roomTypesRepo.findByRoomNameContainingIgnoreCaseAndHotelId(roomTypesDTO.getRoomName(), hotelId);
        if (existingRoomTypes != null) {
            return ResponseClass.responseFailure("Room type with this name already exists in this hotel");
        }

        // Fetch and validate Amenities
        List<Long> invalidAmenitiesIDs = roomTypesDTO.getAmenitiesID().stream()
                .filter(id -> !amenitiesRepo.existsByAmenitiesIdAndHotelId(id, hotelId))
                .collect(Collectors.toList());

        if (!invalidAmenitiesIDs.isEmpty()) {
            return ResponseClass.responseFailure("This Amenities IDs are invalid for this hotel: " + invalidAmenitiesIDs);
        }

        List<Amenities> amenities = amenitiesRepo.findAllById(roomTypesDTO.getAmenitiesID());

        // Fetch and validate Bed Types
        List<Long> invalidBedTypesIDs = roomTypesDTO.getBedTypesID().stream()
                .filter(id -> !bedTypesRepo.existsByBedTypeIdAndHotelId(id, hotelId))
                .collect(Collectors.toList());

        if (!invalidBedTypesIDs.isEmpty()) {
            return ResponseClass.responseFailure("This Bed Types IDs are invalid for this hotel: " + invalidBedTypesIDs);
        }

        List<BedTypes> bedTypes = bedTypesRepo.findAllById(roomTypesDTO.getBedTypesID());

        // Create RoomTypes entity
        RoomTypes roomTypes = new RoomTypes();
        roomTypes.setRoomName(roomTypesDTO.getRoomName());
        roomTypes.setRoomFare(roomTypesDTO.getRoomFare());
//        roomTypes.setNoOfRooms(roomTypesDTO.getNoOfRooms());
        roomTypes.setAdult(roomTypesDTO.getAdult());
        roomTypes.setChildren(roomTypesDTO.getChildren());
        roomTypes.setCancelFee(roomTypesDTO.getCancelFee());
        roomTypes.setFacilities(roomTypesDTO.getFacilities());
        roomTypes.setRoomTypeImage(roomTypesDTO.getRoomTypeImage());
        roomTypes.setRoomTypeStatus(roomTypesDTO.isRoomTypeStatus());
        roomTypes.setFeatureStatus(roomTypesDTO.isFeatureStatus());
        roomTypes.setRoomDescription(roomTypesDTO.getRoomDescription());
        roomTypes.setCancelDescription(roomTypesDTO.getCancelDescription());
        roomTypes.setTotalBed(roomTypes.getTotalBed());
        roomTypes.setHotelId(hotelId);
        roomTypes.setAmenities(amenities);
        roomTypes.setBedTypes(bedTypes);

        roomTypesRepo.save(roomTypes);

        return ResponseClass.responseSuccess("Room Types saved successfully");
    }


    public ResponseEntity<?> updateRoomTypes(
            String token,
            Long id,
            String roomName,
            Double roomFare,
            Integer adult,
            Integer children,
            Double cancelFee,
            String[] facilities,
            MultipartFile[] roomTypeImage,
            Boolean roomTypeStatus,
            Boolean featureStatus,
            String roomDescription,
            String cancelDescription,
            Integer totalBed,
            List<Long> amenitiesID,
            List<Long> bedTypesID) {

        String hotelId = config.tokenValue(token, "hotelId");

        // Fetch existing RoomType by ID
        RoomTypes roomTypes = roomTypesRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room Type not found for this id :: " + id));

        // Check for hotel ownership
        if (!roomTypes.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Unauthorized access to update room type");
        }

        // Check if roomName already exists
        if (roomName != null && !roomName.isEmpty()) {
            RoomTypes existingRoomType = roomTypesRepo.findByRoomNameContainingIgnoreCaseAndHotelId(roomName, hotelId);
            if (existingRoomType != null) {
                return ResponseClass.responseFailure("Room type with this name already exists in this hotel");
            }
            roomTypes.setRoomName(roomName);
        }

        // Update other fields
        if (roomFare != null) {
            roomTypes.setRoomFare(roomFare);
        }
        if (adult != null) {
            roomTypes.setAdult(adult);
        }
        if (children !=null) {
            roomTypes.setChildren(children);
        }
        if (cancelFee !=null) {
            roomTypes.setCancelFee(cancelFee);
        }
        if (facilities != null) {
            roomTypes.setFacilities(facilities);
        }
        if (roomTypeImage != null) {
            // Logic to handle the uploaded images, e.g., save to disk or database
            // Placeholder: set the first image's original filename as a demonstration
//            roomTypes.setRoomTypeImage(roomTypeImage[0].getOriginalFilename());
        }
        if(roomTypeStatus!=null)
        {
            roomTypes.setRoomTypeStatus(roomTypeStatus);
        }
        if(featureStatus!=null)
        {
            roomTypes.setFeatureStatus(featureStatus);
        }

        if (roomDescription != null) {
            roomTypes.setRoomDescription(roomDescription);
        }
        if (cancelDescription != null) {
            roomTypes.setCancelDescription(cancelDescription);
        }
        if (totalBed !=null) {
            roomTypes.setTotalBed(totalBed);
        }

//        // Validate and update Amenities
//        List<Long> invalidAmenitiesIDs = amenitiesID.stream()
//                .filter(id -> !amenitiesRepo.existsByAmenitiesIdAndHotelId(id, hotelId))
//                .collect(Collectors.toList());
//        if (!invalidAmenitiesIDs.isEmpty()) {
//            return ResponseClass.responseFailure("Invalid Amenities IDs: " + invalidAmenitiesIDs);
//        }
        if(amenitiesID!=null)
        {
            List<Amenities> amenities = amenitiesRepo.findAllById(amenitiesID);
            roomTypes.setAmenities(amenities);
        }


//        // Validate and update Bed Types
//        List<Long> invalidBedTypesIDs = bedTypesID.stream()
//                .filter(id -> !bedTypesRepo.existsByBedTypeIdAndHotelId(id, hotelId))
//                .collect(Collectors.toList());
//        if (!invalidBedTypesIDs.isEmpty()) {
//            return ResponseClass.responseFailure("Invalid Bed Types IDs: " + invalidBedTypesIDs);
//        }
        if(bedTypesID!=null)
        {
            List<BedTypes> bedTypes = bedTypesRepo.findAllById(bedTypesID);
            roomTypes.setBedTypes(bedTypes);
        }


        // Save updated RoomTypes entity
        roomTypesRepo.save(roomTypes);

        return ResponseClass.responseSuccess("Room Types updated successfully");
    }

    public ResponseEntity<?> updateRoomTypes1(String token, Long id, RoomTypes roomTypesDetails) {
        String hotelId = config.tokenValue(token, "hotelId");
        RoomTypes roomTypes = roomTypesRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Room Type not found for this id :: " + id));

        if (!roomTypes.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Unauthorized access to update room type");
        }

        if(roomTypesDetails.getRoomName()!= null && !roomTypesDetails.getRoomName().isEmpty()) {
            RoomTypes roomTypes1 = roomTypesRepo.findByRoomNameContainingIgnoreCaseAndHotelId(roomTypes.getRoomName(), hotelId);
            if (roomTypes1 != null) {
                return ResponseClass.responseFailure("Room type with this name already exists in this hotel");
            }
        }
        // Conditional field updates
        if (roomTypesDetails.getRoomName() != null) {
            roomTypes.setRoomName(roomTypesDetails.getRoomName());
        }

        if (roomTypesDetails.getRoomFare() != null) {
            roomTypes.setRoomFare(roomTypesDetails.getRoomFare());
        }

//        if (roomTypesDetails.getNoOfRooms() != 0) {
//            roomTypes.setNoOfRooms(roomTypesDetails.getNoOfRooms());
//        }

        if (roomTypesDetails.getAdult() != 0) {
            roomTypes.setAdult(roomTypesDetails.getAdult());
        }

        if (roomTypesDetails.getChildren() != 0) {
            roomTypes.setChildren(roomTypesDetails.getChildren());
        }

        if (roomTypesDetails.getCancelFee() != 0) {
            roomTypes.setCancelFee(roomTypesDetails.getCancelFee());
        }

        if (roomTypesDetails.getFacilities() != null) {
            roomTypes.setFacilities(roomTypesDetails.getFacilities());
        }

        if (roomTypesDetails.getRoomTypeImage() != null) {
            roomTypes.setRoomTypeImage(roomTypesDetails.getRoomTypeImage());
        }

        roomTypes.setRoomTypeStatus(roomTypesDetails.isRoomTypeStatus());
        roomTypes.setFeatureStatus(roomTypesDetails.isFeatureStatus());

        if (roomTypesDetails.getRoomDescription() != null) {
            roomTypes.setRoomDescription(roomTypesDetails.getRoomDescription());
        }

        if (roomTypesDetails.getCancelDescription() != null) {
            roomTypes.setCancelDescription(roomTypesDetails.getCancelDescription());
        }

        if (roomTypesDetails.getAmenities() != null && !roomTypesDetails.getAmenities().isEmpty()) {
            roomTypes.setAmenities(roomTypesDetails.getAmenities());
        }

        if (roomTypesDetails.getBedTypes() != null && !roomTypesDetails.getBedTypes().isEmpty()) {
            roomTypes.setBedTypes(roomTypesDetails.getBedTypes());
        }

        roomTypesRepo.save(roomTypes);
        return ResponseClass.responseSuccess("Room Types updated successfully");
    }


    public ResponseEntity<?> getRoomTypesById(String token, Long id) {
            String hotelId = config.tokenValue(token, "hotelId");
            RoomTypes roomTypes = roomTypesRepo.findByRoomTypesId(id);

            if (roomTypes == null && !roomTypes.getHotelId().equals(hotelId)) {
                return ResponseClass.responseSuccess("Room Types not found");
            }
            return ResponseClass.responseSuccess("Room Types retrieved successfully", "roomTypes", roomTypes);
        }



    public ResponseEntity<?> getAllRoomTypes(String token) {
        String hotelId = config.tokenValue(token, "hotelId");
        List<RoomTypes> roomTypesList = roomTypesRepo.findByHotelId(hotelId);
        return ResponseClass.responseSuccess("All Room Types data","roomTypes",roomTypesList);
    }

    public ResponseEntity<?> deleteRoomTypesById(String token, Long id) {
        String hotelId = config.tokenValue(token, "hotelId");
        RoomTypes roomTypes = roomTypesRepo.findByRoomTypesId(id);

        if (roomTypes == null && !(roomTypes.getHotelId().equals(hotelId))) {
            return ResponseClass.responseSuccess("Room Types not found");
        }
        roomTypesRepo.delete(roomTypes);
        return ResponseClass.responseSuccess("Room Types deleted successfully");
    }

    public ResponseEntity<?> createRoomTypes(
            String token,
            String roomName,
            Double roomFare,
            int adult,
            int children,
            double cancelFee,
            String[] facilities,
            MultipartFile[] roomTypeImage,
            boolean roomTypeStatus,
            boolean featureStatus,
            String roomDescription,
            String cancelDescription,
            int totalBed,
            List<Long> amenitiesID,
            List<Long> bedTypesID) {

        // Extract hotelId from token
        String hotelId = config.tokenValue(token, "hotelId");

        // Check if room type already exists in the hotel
        RoomTypes existingRoomTypes = roomTypesRepo.findByRoomNameContainingIgnoreCaseAndHotelId(roomName, hotelId);
        if (existingRoomTypes != null) {
            return ResponseClass.responseFailure("Room type with this name already exists in this hotel");
        }

        // Fetch and validate Amenities
        List<Long> invalidAmenitiesIDs = amenitiesID.stream()
                .filter(id -> !amenitiesRepo.existsByAmenitiesIdAndHotelId(id, hotelId))
                .collect(Collectors.toList());

        if (!invalidAmenitiesIDs.isEmpty()) {
            return ResponseClass.responseFailure("These Amenities IDs are invalid for this hotel: " + invalidAmenitiesIDs);
        }

        List<Amenities> amenities = amenitiesRepo.findAllById(amenitiesID);

        // Fetch and validate Bed Types
        List<Long> invalidBedTypesIDs = bedTypesID.stream()
                .filter(id -> !bedTypesRepo.existsByBedTypeIdAndHotelId(id, hotelId))
                .collect(Collectors.toList());

        if (!invalidBedTypesIDs.isEmpty()) {
            return ResponseClass.responseFailure("These Bed Types IDs are invalid for this hotel: " + invalidBedTypesIDs);
        }

        List<BedTypes> bedTypes = bedTypesRepo.findAllById(bedTypesID);

        // Create RoomTypes entity
        RoomTypes roomTypes = new RoomTypes();
        roomTypes.setRoomName(roomName);
        roomTypes.setRoomFare(roomFare);
        roomTypes.setAdult(adult);
        roomTypes.setChildren(children);
        roomTypes.setCancelFee(cancelFee);
        roomTypes.setFacilities(facilities);
        List<String> imageNames = new ArrayList<>();
        if (roomTypeImage != null) {
            for (MultipartFile file : roomTypeImage) {
                imageNames.add(file.getOriginalFilename());
            }
        }
        roomTypes.setRoomTypeImage(imageNames.toArray(new String[0]));
        roomTypes.setRoomTypeStatus(roomTypeStatus);
        roomTypes.setFeatureStatus(featureStatus);
        roomTypes.setRoomDescription(roomDescription);
        roomTypes.setCancelDescription(cancelDescription);
        roomTypes.setTotalBed(totalBed);
        roomTypes.setHotelId(hotelId);
        roomTypes.setAmenities(amenities);
        roomTypes.setBedTypes(bedTypes);

        roomTypesRepo.save(roomTypes);

        return ResponseClass.responseSuccess("Room Types saved successfully");
    }

}
