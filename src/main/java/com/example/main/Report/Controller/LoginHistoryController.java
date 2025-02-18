package com.example.main.Report.Controller;

import com.example.main.Report.Service.LoginHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/report/login/history")
public class LoginHistoryController {
    @Autowired
    private LoginHistoryService loginHistoryService;
    @GetMapping
    @RequestMapping("/getAll")
    public ResponseEntity<?> getLoginHistory(@RequestHeader("Authorization") String token,
                                             @RequestParam(value = "search",required = false) String email){
        return loginHistoryService.getLoginHistory(email);

    }
}
