package com.example.main.Report.Service;

import com.example.main.Exception.ResponseClass;
import com.example.main.Login.Entity.LoginModel;
import com.example.main.Report.Repo.LoginHistoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginHistoryService {
    @Autowired
    private LoginHistoryRepo loginHistoryRepo;

    public ResponseEntity<?> getLoginHistory(String email){
        List<LoginModel> a=loginHistoryRepo.findAll();
        return ResponseClass.responseSuccess("Login details success","loginModal",a);

    }
}
