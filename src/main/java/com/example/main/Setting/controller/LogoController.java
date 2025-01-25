package com.example.main.Setting.controller;

import com.example.main.Setting.entity.GeneralSetting;
import com.example.main.Setting.service.LogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/logo")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LogoController {

    private final LogoService logoService;

    @PostMapping("/add")
    public ResponseEntity<?> createLogo(@RequestHeader("Authorization") String token, @RequestParam(required = false) MultipartFile logoWhite,@RequestParam(required = false) MultipartFile logoBlack,@RequestParam(required = false) MultipartFile favicon){
        return  logoService.createLogo(token,logoWhite,logoBlack,favicon);

    }

    @GetMapping("/getByHotelId")
    public ResponseEntity<?> getLogo(@RequestHeader("Authorization") String token){
        return  logoService.getLogo(token);

    }


    @PutMapping("/updateByHotel")
    public ResponseEntity<?> updateLogo(@RequestHeader("Authorization") String token, @RequestParam(required = false) MultipartFile logoWhite,@RequestParam(required = false) MultipartFile logoBlack,@RequestParam MultipartFile favicon){
        return  logoService.updateLogo(token,logoWhite,logoBlack,favicon);

    }
}
