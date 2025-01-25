package com.example.main.Setting.service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.Setting.entity.Logo;
import com.example.main.Setting.repo.LogoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LogoService {

private final LogoRepo logoRepo;

private final ConfigClass configClass;

    public ResponseEntity<?> createLogo(String token, MultipartFile logoWhite, MultipartFile logoBlack, MultipartFile favicon) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        Logo logo = logoRepo.findByHotelId(hotelId);

        if (logo == null) {
            logo = new Logo(); // Create a new logo if not found
            logo.setHotelId(hotelId);
        }

        // Update or save images if provided
        try {
            if (logoWhite != null) logo.setLogoWhite(saveImage(logoWhite));
            if (logoBlack != null) logo.setLogoBlack(saveImage(logoBlack));
            if (favicon != null) logo.setFavicon(saveImage(favicon));
        } catch (IOException e) {
            return ResponseClass.responseFailure("Failed to store image: " + e.getMessage());
        }

        logoRepo.save(logo);
        return ResponseClass.responseSuccess("Logo updated successfully");
    }

    private String saveImage(MultipartFile image) throws IOException {
        String imagePath = ConfigClass.saveImage(image);
        if (imagePath == null) {
            throw new IOException("Image path is null");
        }
        return imagePath;
    }


    public ResponseEntity<?> getLogo(String token) {
        String hotelId = configClass.tokenValue(token,"hotelId");
        Logo logo = logoRepo.findByHotelId(hotelId);
        if(logo==null)
        {
            return ResponseClass.responseFailure("Logo not found for this hotel");
        }
        return ResponseClass.responseSuccess("Logo retrieved successfully", "logo", logo);

    }

    public ResponseEntity<?> updateLogo(String token, MultipartFile logoWhite, MultipartFile logoBlack, MultipartFile favicon) {
        String hotelId = configClass.tokenValue(token,"hotelId");
        Logo logo = logoRepo.findByHotelId(hotelId);
        if(logo==null)
        {
            return ResponseClass.responseFailure("Logo not found for this hotel");
        }

        if(logoWhite != null){
            try {
                String imagePath = ConfigClass.saveImage(logoWhite);
                if (imagePath != null) {
                    logo.setLogoWhite(imagePath);
                }
            } catch (IOException e) {
                return ResponseClass.responseFailure("Failed to store image");
            }
        }
        if(logoBlack != null){
            try {
                String imagePath = ConfigClass.saveImage(logoWhite);
                if (imagePath != null) {
                    logo.setLogoBlack(imagePath);
                }
            } catch (IOException e) {
                return ResponseClass.responseFailure("Failed to store image");
            }
        }
        if(favicon != null) {
            try {
                String imagePath = ConfigClass.saveImage(favicon);
                if (imagePath != null) {
                    logo.setFavicon(imagePath);
                }
            } catch (IOException e) {
                return ResponseClass.responseFailure("Failed to store image");
            }
        }
        logoRepo.save(logo);
        return ResponseClass.responseSuccess("Logo updated successfully");

    }
}
