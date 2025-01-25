package com.example.main.ManageHotel.Controller;


import com.example.main.ManageHotel.Service.BedTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/bedTypes")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BedTypeController {

    private final BedTypeService bedTypeService;

    @PostMapping("/add")
    ResponseEntity<?> createBedType(@RequestHeader("Authorization") String token,
                                    @RequestParam String bedName, @RequestParam boolean status,
                                    @RequestParam(required = false) MultipartFile bedImage) {
        return bedTypeService.createBedType(token, bedName, status, bedImage);
    }

    @PostMapping("/update/{id}")
    ResponseEntity<?> updateBedType(@RequestHeader("Authorization") String token,
                                    @PathVariable("id") long bedTypeId,
                                    @RequestParam(required = false) String bedName, @RequestParam(required = false) Boolean status,
                                    @RequestParam(required = false) MultipartFile bedImage) {
        return bedTypeService.updateBedType(token, bedTypeId, bedName, status, bedImage);
    }


    @GetMapping("/getAll")
    public ResponseEntity<?> getAllBedTypes(@RequestHeader("Authorization") String token) {
        return bedTypeService.getAllBedTypes(token);
    }

    @GetMapping("/getById/{bedTypeId}")
    public ResponseEntity<?> getBedTypeById(@RequestHeader("Authorization") String token,
                                            @PathVariable("bedTypeId") long bedTypeId) {
        return bedTypeService.getBedTypeById(token, bedTypeId);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteBedTypeById(@RequestHeader("Authorization") String token,
                                               @PathVariable("id") long bedTypeId) {
        return bedTypeService.deleteBedTypeById(token, bedTypeId);
    }
}
