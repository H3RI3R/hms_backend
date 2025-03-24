package com.example.main.Report.Controller;

import com.example.main.Report.Service.LoginHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/report/login/history")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LoginHistoryController {
    @Autowired
    private LoginHistoryService loginHistoryService;
    @GetMapping("/getAll")
    public ResponseEntity<?> getLoginHistory(
            @RequestHeader("Authorization") String token,
            @RequestParam(value = "search", required = false) String email,
            @RequestParam(value = "fromDate", required = false) String fromDate,
            @RequestParam(value = "toDate", required = false) String toDate) {
        return loginHistoryService.getLoginHistory(email, fromDate, toDate);
    }
}
