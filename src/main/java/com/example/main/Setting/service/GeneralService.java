package com.example.main.Setting.service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.Setting.entity.GeneralSetting;
import com.example.main.Setting.entity.Logo;
import com.example.main.Setting.repo.GeneralRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GeneralService {

    private final GeneralRepo generalRepo;

    private final ConfigClass configClass;

    public ResponseEntity<?> createGeneral(String token, GeneralSetting generalSetting) {
        String hotelId = configClass.tokenValue(token, "hotelId");
        GeneralSetting  setting = generalRepo.findByHotelId(hotelId);

        if (setting == null) {
            setting = new GeneralSetting();
            setting.setHotelId(hotelId);
        }

        setting.setSiteTitle(generalSetting.getSiteTitle() != null ? generalSetting.getSiteTitle() : null);
        setting.setCurrency(generalSetting.getCurrency() != null ? generalSetting.getCurrency() : null);
        setting.setTimeZone(generalSetting.getTimeZone() != null ? generalSetting.getTimeZone() : null);
        setting.setSiteBaseColor(generalSetting.getSiteBaseColor() != null ? generalSetting.getSiteBaseColor() : null);
        setting.setSiteRecordPage(generalSetting.getSiteRecordPage() != 0 ? generalSetting.getSiteRecordPage() : 0);
        setting.setCurrencyFormat(generalSetting.getCurrencyFormat() != null ? generalSetting.getCurrencyFormat() : null);
        setting.setTaxName(generalSetting.getTaxName() != null ? generalSetting.getTaxName() : null);
        setting.setTaxPercent(generalSetting.getTaxPercent() != 0 ? generalSetting.getTaxPercent() : 0);
        setting.setCheckOutTime(generalSetting.getCheckOutTime() != null ? generalSetting.getCheckOutTime() : null);
        setting.setCheckInTime(generalSetting.getCheckInTime() != null ? generalSetting.getCheckInTime() : null);
        setting.setUpComingCheckInList(generalSetting.getUpComingCheckInList() != 0 ? generalSetting.getUpComingCheckInList() : 0);
        setting.setUpComingCheckOutList(generalSetting.getUpComingCheckOutList() != 0 ? generalSetting.getUpComingCheckOutList() : 0);
        // Save the updated or newly created GeneralSetting
        generalRepo.save(setting);

        return ResponseClass.responseSuccess("GeneralSetting saved successfully");

    }

    public ResponseEntity<?> getById(String token) {
        String hotelId = configClass.tokenValue(token,"hotelId");
        GeneralSetting generalSetting = generalRepo.findByHotelId(hotelId);
        if(generalSetting==null)
        {
            return ResponseClass.responseFailure("General Setting not found for this hotel");
        }
        return ResponseClass.responseSuccess("General Setting retrieved successfully", "generalSetting", generalSetting);
    }

    public ResponseEntity<?> updateHotel(String token, GeneralSetting generalSetting) {
        // Retrieve the hotelId from the token
        String hotelId = configClass.tokenValue(token, "hotelId");

        // Find the existing general setting for the hotel
        GeneralSetting generalSetting1 = generalRepo.findByHotelId(hotelId);

        // If no setting is found, return failure response
        if (generalSetting1 == null) {
            return ResponseClass.responseFailure("General Setting not found for this hotel");
        }

        // Update the fields of the existing general setting with new values if provided
        if (generalSetting.getSiteTitle() != null) {
            generalSetting1.setSiteTitle(generalSetting.getSiteTitle());
        }
        if (generalSetting.getCurrency() != null) {
            generalSetting1.setCurrency(generalSetting.getCurrency());
        }
        if (generalSetting.getTimeZone() != null) {
            generalSetting1.setTimeZone(generalSetting.getTimeZone());
        }
        if (generalSetting.getSiteBaseColor() != null) {
            generalSetting1.setSiteBaseColor(generalSetting.getSiteBaseColor());
        }
        if (generalSetting.getSiteRecordPage() != 0) {
            generalSetting1.setSiteRecordPage(generalSetting.getSiteRecordPage());
        }
        if (generalSetting.getCurrencyFormat() != null) {
            generalSetting1.setCurrencyFormat(generalSetting.getCurrencyFormat());
        }
        if (generalSetting.getTaxName() != null) {
            generalSetting1.setTaxName(generalSetting.getTaxName());
        }
        if (generalSetting.getTaxPercent() != 0.0f) {
            generalSetting1.setTaxPercent(generalSetting.getTaxPercent());
        }
        if (generalSetting.getCheckOutTime() != null) {
            generalSetting1.setCheckOutTime(generalSetting.getCheckOutTime());
        }
        if (generalSetting.getCheckInTime() != null) {
            generalSetting1.setCheckInTime(generalSetting.getCheckInTime());
        }
        if (generalSetting.getUpComingCheckInList() != 0) {
            generalSetting1.setUpComingCheckInList(generalSetting.getUpComingCheckInList());
        }
        if (generalSetting.getUpComingCheckOutList() != 0) {
            generalSetting1.setUpComingCheckOutList(generalSetting.getUpComingCheckOutList());
        }

        // Save the updated general setting to the repository
        generalRepo.save(generalSetting1);

        // Return success response with the updated data
        return ResponseClass.responseSuccess("General Setting updated successfully", "generalSetting", generalSetting1);
    }

}
