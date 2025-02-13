package com.example.main.Searching.Controller;


import com.example.main.Configuration.ConfigClass;
import com.example.main.Searching.Service.SearchingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/staff/search")
public class SearchingController {
    @Autowired
    private SearchingService searchingService;
    @Autowired
    private ConfigClass configClass;

    @GetMapping("/{email}")
    public ResponseEntity<?> searchStaffByEmail(
            @RequestHeader("Authorization") String token,
            @PathVariable("email") String email) {

        String hotelId = configClass.tokenValue(token, "hotelId");
        return searchingService.searchStaffByEmail(hotelId, email);
    }
}
