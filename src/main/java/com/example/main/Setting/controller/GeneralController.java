package com.example.main.Setting.controller;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Setting.entity.GeneralSetting;
import com.example.main.Setting.service.GeneralService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Time;

@RestController
@RequestMapping("/general")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GeneralController {

    private final GeneralService generalService;
    private final ConfigClass configClass;

    @PostMapping("/add")
    public ResponseEntity<?> createGeneral(@RequestHeader("Authorization") String token, @RequestBody GeneralSetting generalSetting){

        return  generalService.createGeneral(token,generalSetting);

    }

    @GetMapping("/getByHotelId")
    public ResponseEntity<?> getById(@RequestHeader("Authorization") String token){
        return  generalService.getById(token);

    }


    @PutMapping("/updateByHotel")
    public ResponseEntity<?> update(@RequestHeader("Authorization") String token, @RequestBody GeneralSetting generalSetting){
        return  generalService.updateHotel(token,generalSetting);

    }


}
