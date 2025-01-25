package com.example.main.ManageHotel.Controller;

import com.example.main.ManageHotel.Entity.PremiumService;
import com.example.main.ManageHotel.Service.PremiumServiceClass;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/preServ")
@RestController
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class PremiumServiceController {

    private final PremiumServiceClass premiumServiceService;


    @PostMapping("/add")
    public ResponseEntity<?> addPremiumService(@RequestHeader("Authorization") String token, @RequestBody PremiumService premiumService) {
        return premiumServiceService.createPremiumService(token, premiumService);
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<?> getPremiumServiceById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return premiumServiceService.getPremiumServiceById(token, id);
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllPremiumServices(@RequestHeader("Authorization") String token) {
        return premiumServiceService.getAllPremiumServices(token);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updatePremiumServiceById(@RequestHeader("Authorization") String token, @PathVariable Long id, @RequestParam(required = false) String preSerName,@RequestParam(required = false) Boolean status, @RequestParam(required = false) Double price) {
        return premiumServiceService.updatePremiumServiceById(token, id, preSerName, status, price);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deletePremiumServiceById(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        return premiumServiceService.deletePremiumServiceById(token, id);
    }
}
