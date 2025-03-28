package com.example.main.Report.Service;

import com.example.main.Exception.ResponseClass;
import com.example.main.Login.Entity.LoginModel;
import com.example.main.Report.Repo.LoginHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LoginHistoryService {
    @Autowired
    private LoginHistoryRepo loginHistoryRepo;

    public ResponseEntity<?> getLoginHistory(String email, String fromDate, String toDate) {
        List<LoginModel> loginRecords;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime fromDateTime = null;
        LocalDateTime toDateTime = null;

        if (fromDate != null && !fromDate.isEmpty()) {
            fromDateTime = LocalDate.parse(fromDate, formatter).atStartOfDay();
        }
        if (toDate != null && !toDate.isEmpty()) {
            toDateTime = LocalDate.parse(toDate, formatter).atTime(23, 59, 59);
        }

        if (fromDateTime != null && toDateTime != null && toDateTime.isBefore(fromDateTime)) {
            return ResponseClass.responseFailure("Invalid date range: 'toDate' cannot be earlier than 'fromDate'.");
        }

        if (fromDateTime != null && toDateTime == null) {
            toDateTime = LocalDateTime.now();
        } else if (toDateTime != null && fromDateTime == null) {
            fromDateTime = LocalDate.of(2000, 1, 1).atStartOfDay();
        }

        if (email != null && !email.isEmpty()) {
            loginRecords = loginHistoryRepo.findByEmail(email);
            if (loginRecords.isEmpty()) {
                return ResponseClass.responseFailure("No record found with this email: " + email);
            }
            return ResponseClass.responseSuccess("Login details for user success", "loginModal", loginRecords);
        }

        if (fromDateTime != null && toDateTime != null) {
            loginRecords = loginHistoryRepo.findByLoginAtBetween(fromDateTime, toDateTime);
        } else {
            loginRecords = loginHistoryRepo.findAll();
        }

        if (loginRecords.isEmpty()) {
            return ResponseClass.responseFailure("No login history found for the given filters.");
        }

        return ResponseClass.responseSuccess("Login details success", "loginModal", loginRecords);
    }
}
