package com.example.main.ReportAndRequest.Service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.Login.Entity.LoginModel;
import com.example.main.Login.Repo.LoginRepo;
import com.example.main.Login.Service.LoginService;
import com.example.main.ReportAndRequest.Entity.ReportAndRequest;
import com.example.main.ReportAndRequest.Repo.ReportAndRequestRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportAndRequestService {
    @Autowired
    private ReportAndRequestRepo reportAndRequestRepo;
    @Autowired
    private ConfigClass configClass;
    @Autowired
    private LoginRepo loginRepo;

    public ResponseEntity<?> getAllReportAndRequest(){
        String r = "fer";
        return ResponseClass.responseSuccess(r);
    }
//create report
    public ResponseEntity<Map<String, Object>> createReport(String token, String type, String message) {
        try {
            HashMap<String, String> tokenValues = configClass.getFromToken(token);
            String email = tokenValues.get("email");
            LoginModel user = loginRepo.findByEmail(email);
            if (user == null) {
                throw new RuntimeException("User not found with email: " + email);
            }
//            System.out.println(user);
            String userName = user.getUserName();
            String userEmail = user.getEmail();

            ReportAndRequest report = new ReportAndRequest();
            report.setType(type);
            report.setMessage(message);
            report.setStatus("Active");
            report.setUserName(userName);
            report.setUserEmail(userEmail);

            ReportAndRequest savedReport = reportAndRequestRepo.save(report);

            Map<String, Object> reportData = prepareReportData(savedReport);
            return ResponseClass.responseSuccess("Report created successfully", "report", reportData);
        } catch (RuntimeException e) {
            return ResponseClass.responseFailure(e.getMessage());
        } catch (Exception e) {
            return ResponseClass.internalServer("An error occurred while creating the report");
        }
    }
//get all
    public ResponseEntity<Map<String, Object>> getAllReports() {
        try {
            List<ReportAndRequest> reports = reportAndRequestRepo.findAll();
            if (reports.isEmpty()) {
                return ResponseClass.responseFailure("No reports found");
            }
            return ResponseClass.responseSuccess("Reports fetched successfully", "reports", reports);
        } catch (Exception e) {
            return ResponseClass.internalServer("An error occurred while fetching the reports");
        }
    }

    private Map<String, Object> prepareReportData(ReportAndRequest savedReport) {
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("id", savedReport.getId());
        reportData.put("userName", savedReport.getUserName());
        reportData.put("userEmail", savedReport.getUserEmail());
        reportData.put("type", savedReport.getType());
        reportData.put("message", savedReport.getMessage());
        reportData.put("status", savedReport.getStatus());
        return reportData;
    }

}
