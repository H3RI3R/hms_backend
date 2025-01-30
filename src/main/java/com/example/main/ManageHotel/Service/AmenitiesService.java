package com.example.main.ManageHotel.Service;

import com.example.main.Configuration.CacheService;
import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.Hotel.Service.NotificationService;
import com.example.main.ManageHotel.Entity.Amenities;
import com.example.main.ManageHotel.Repo.AmenitiesRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AmenitiesService {

    private final AmenitiesRepo amenitiesRepo;
    private final ConfigClass config;
    private final NotificationService notificationService;


    private final CacheService cacheService;

    private static final String AMENITIES_CACHE_KEY = "amenitiesCache";


    public ResponseEntity<?> createAmenities(String hotelId, String amenitiesName, boolean status, MultipartFile icon) {
        Amenities amenities1 = amenitiesRepo.findByAmenitiesNameContainingIgnoreCaseAndHotelId(amenitiesName, hotelId);
        if(amenities1 != null)
        {
            return ResponseClass.responseFailure("This amenities already exists in this Hotel");
        }
        Amenities amenities = new Amenities();
        amenities.setAmenitiesName(amenitiesName);
        amenities.setStatus(status);
        amenities.setHotelId(hotelId);
        if(icon != null){
            try {
                String imagePath = ConfigClass.saveImage(icon);
                if (imagePath != null) {
                    amenities.setIcon(imagePath);
                }
            } catch (IOException e) {
                return ResponseClass.responseFailure("Failed to store image");
            }
        }
        amenitiesRepo.save(amenities);
        notificationService.sendHotelAddedNotification(amenitiesName);
        updateAmenitiesCache(hotelId);

        return ResponseClass.responseSuccess("Amenities added successfully");
    }

       // @CacheEvict(value = "amenitiesCache", key = "#token")
    public ResponseEntity<?> updateAmenities(String token, long amenitiesId, String amenitiesName, Boolean status, MultipartFile icon) {
        String hotelId = config.tokenValue(token, "hotelId");
//        if (!roleType.equals("ADMIN")) {
//            return ResponseClass.responseFailure("Role is not admin");
//        }
        Amenities amenities = amenitiesRepo.findByAmenitiesId(amenitiesId);
        if (amenities == null) {
            return ResponseClass.responseFailure("Amenities not found");
        }

        Amenities amenities1 = amenitiesRepo.findByAmenitiesNameContainingIgnoreCaseAndHotelId(amenitiesName, hotelId);
        if(amenities1 != null)
        {
            return ResponseClass.responseFailure("This amenities already exists in this Hotel");
        }

        if (amenitiesName != null) {
            amenities.setAmenitiesName(amenitiesName);
        }

        if (status != null) {
            amenities.setStatus(status);
        }

        if (icon != null && !icon.isEmpty()) {
            try {
                String imagePath = ConfigClass.saveImage(icon);
                if (imagePath != null) {
                    amenities.setIcon(imagePath);
                }
            } catch (IOException e) {
                return ResponseClass.responseFailure("Failed to store image");
            }
        }

        amenitiesRepo.save(amenities);
//        updateAmenitiesCache(hotelId);

        return ResponseClass.responseSuccess("Amenities updated successfully");
    }

    @Cacheable(value = AMENITIES_CACHE_KEY, key = "'hotelId:' + #hotelId")
    public List<Amenities> getAmenitiesByHotelId(String hotelId) {
        List<Amenities> amenities = amenitiesRepo.findByHotelId(hotelId);

        return amenities;
    }


    public ResponseEntity<?> getAllAmenities(String token) {

        String hotelId = config.tokenValue(token, "hotelId");
        String cacheKey = "hotelId:" + hotelId;  // Unique key for caching by hotel ID
        List<Amenities> amenities = amenitiesRepo.findByHotelId(hotelId);
        // Attempt to retrieve from cache
//        List<Amenities> amenitiesList = (List<Amenities>) cacheService.getFromHashCache(AMENITIES_CACHE_KEY, cacheKey);

//        if (amenitiesList == null) {
//            amenitiesList = amenitiesRepo.findByHotelId(hotelId);
//            if (amenitiesList != null && !amenitiesList.isEmpty()) {
//                cacheService.putInHashCache(AMENITIES_CACHE_KEY, cacheKey, amenitiesList);
//            }
//        }

//        return ResponseClass.responseSuccess("Amenities retrieved successfully", "Amenities", amenities);
        return (ResponseEntity<?>) ResponseEntity.internalServerError();
    }

    @CacheEvict(value = AMENITIES_CACHE_KEY, key = "'hotelId:' + #hotelId")
    public ResponseEntity<?> deleteAmenitiesById(String token, long amenitiesId) {
        String hotelId = config.tokenValue(token, "hotelId");
        Amenities amenity = amenitiesRepo.findByAmenitiesId(amenitiesId);

        if (amenity == null || !amenity.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Amenity not found or does not belong to the specified hotel");
        }
        amenitiesRepo.delete(amenity);
        String cacheKey = "hotelId:" + hotelId;
        cacheService.deleteFromCache(AMENITIES_CACHE_KEY, cacheKey);

        return ResponseClass.responseSuccess("Amenity deleted successfully");
    }

    private void updateAmenitiesCache(String hotelId) {
        List<Amenities> updatedAmenitiesList = amenitiesRepo.findByHotelId(hotelId);
        String cacheKey = "hotelId:" + hotelId;
        cacheService.putInHashCache(AMENITIES_CACHE_KEY, cacheKey, updatedAmenitiesList); // Use CacheService for updating the cache
    }



    public ResponseEntity<?> getAmenitiesById(String token, long amenitiesId) {
        String hotelId = config.tokenValue(token, "hotelId");
        Amenities amenity = amenitiesRepo.findByAmenitiesId(amenitiesId);
//        String cacheKey = "amenity:" + amenitiesId;
//        Amenities amenity = cacheService.getOrCache(AMENITIES_CACHE_KEY, cacheKey, () -> {
//            Amenities fetchedAmenity = amenitiesRepo.findByAmenitiesId(amenitiesId);
//            if (fetchedAmenity == null || !fetchedAmenity.getHotelId().equals(hotelId)) {
//                return null;
//            }
//
//            return fetchedAmenity;
//        });

        if (amenity == null) {
            return ResponseClass.responseFailure("Amenity not found or does not belong to the specified hotel");
        }

        return ResponseClass.responseSuccess("Amenity retrieved successfully", "amenity", amenity);
    }



}
