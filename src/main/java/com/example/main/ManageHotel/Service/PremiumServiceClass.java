package com.example.main.ManageHotel.Service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.ManageHotel.Entity.PremiumService;
import com.example.main.ManageHotel.Repo.PremiumServiceRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PremiumServiceClass {

    private final ConfigClass config;
    private final PremiumServiceRepo premiumServiceRepo;

    // Create Premium Service
    public ResponseEntity<?> createPremiumService(String token, PremiumService premiumService) {
        String hotelId = config.tokenValue(token, "hotelId");
        premiumService.setHotelId(hotelId);

        PremiumService existingService = premiumServiceRepo.findByPreSerNameContainingIgnoreCaseAndHotelId(premiumService.getPreSerName(), hotelId);
        if (existingService != null) {
            return ResponseClass.responseFailure("Premium service with this name already exists in this hotel");
        }

        premiumServiceRepo.save(premiumService);
        return ResponseClass.responseSuccess("Premium service added successfully");
    }

    // Get Premium Service by ID
    public ResponseEntity<?> getPremiumServiceById(String token, Long id) {
        String hotelId = config.tokenValue(token, "hotelId");
        PremiumService premiumService = premiumServiceRepo.findByPreServiceId(id);
        if (premiumService == null || !premiumService.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Premium service not found in this hotel");
        }
        return ResponseClass.responseSuccess("Premium service found in this hotel","premiumService",premiumService);
    }

    // Get All Premium Services
    public ResponseEntity<?> getAllPremiumServices(String token) {
        String hotelId = config.tokenValue(token, "hotelId");
        List<PremiumService> premiumServices = premiumServiceRepo.findByHotelId(hotelId);
        return ResponseEntity.ok(premiumServices);
    }

    // Update Premium Service by ID
    public ResponseEntity<?> updatePremiumServiceById(String token, Long id,  String preSerName,  Boolean status,  Double price) {
        String hotelId = config.tokenValue(token, "hotelId");
        PremiumService premiumService = premiumServiceRepo.findByPreServiceId(id);

        if (premiumService == null || !premiumService.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Premium service not found in this hotel");
        }

        if (preSerName != null) {
            PremiumService existingService = premiumServiceRepo.findByPreSerNameContainingIgnoreCaseAndHotelId(preSerName, hotelId);
            if (existingService != null) {
                return ResponseClass.responseFailure("Premium service with this name already exists in this hotel");
            }
            premiumService.setPreSerName(preSerName);
        }

        if (status != null) { // Assuming `status` is a Boolean in PremiumService
            premiumService.setStatus(status);
        }

        if(price!=null)
        {
            premiumService.setPrice(price);
        }

        premiumServiceRepo.save(premiumService);
        return ResponseClass.responseSuccess("Premium service update successfully");
    }

    // Delete Premium Service by ID
    public ResponseEntity<?> deletePremiumServiceById(String token, Long id) {
        String hotelId = config.tokenValue(token, "hotelId");
        PremiumService premiumService = premiumServiceRepo.findByPreServiceId(id);

        if (premiumService == null || !premiumService.getHotelId().equals(hotelId)) {
            return ResponseClass.responseFailure("Premium service not found in this hotel");
        }

        premiumServiceRepo.deleteById(id);
        return ResponseEntity.ok("Premium service deleted successfully");
    }



}
