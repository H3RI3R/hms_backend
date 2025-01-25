package com.example.main.Hotel.Service;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.Hotel.Entity.Admin;
import com.example.main.Hotel.Repo.AdminRepo;
import com.example.main.Login.Entity.LoginModel;
import com.example.main.Login.Repo.LoginRepo;
import com.example.main.Login.Repo.OtpRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;


@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class AdminService {

    private final AdminRepo adminRepo;
    private final LoginRepo loginRepo;
    private final PasswordEncoder passwordEncoder;
    private final ConfigClass configClass;
    private final OtpRepo otpRepo;


    public ResponseEntity<?> createSuperAdmin(String secret, String adminName, String adminAddress, String adminEmail, String adminPhone, String adminPassword) {

        String secretKey = "hotel987654321987654321";
        if(secretKey.equals(secret)){
            Admin adminExist = adminRepo.findByAdminEmail(adminEmail);
            if(adminExist != null){
                return ResponseClass.responseFailure("admin email already exits");
            }
            LoginModel adminLoginExist = loginRepo.findByEmail(adminEmail);
            if(adminLoginExist != null){
                return ResponseClass.responseFailure("admin email already exits in login table");
            }

            String superAdminId = UUID.randomUUID().toString();
            Admin admin = new Admin();
            try {

                admin.setAdminEmail(adminEmail);
                admin.setRoleType("SUPERADMIN");
                admin.setHotelId(superAdminId);
                admin.setAdminName(adminName);
                admin.setAdminPhoneNo(adminPhone);
                admin.setAdminAddress(adminAddress);
                admin.setAdminPassword(passwordEncoder.encode(adminPassword));
                adminRepo.save(admin);

                LoginModel adminLogin = new LoginModel();
                adminLogin.setRole("SUPERADMIN");
                adminLogin.setEmail(adminEmail);
                adminLogin.setPassword(adminPassword);
                adminLogin.setPassword(passwordEncoder.encode(adminPassword));
                adminLogin.setHotelId(superAdminId);
                loginRepo.save(adminLogin);
                return ResponseClass.responseSuccess("super admin created successfully");
            }catch (Exception e){
                return ResponseClass.responseFailure("failure in generating SuperAdmin");
            }
        }
        return ResponseClass.responseFailure("Invalid Secret Key");
    }

    public ResponseEntity<?> getAllAdmin(String token) {
        String roleType = configClass.tokenValue(token,"roleType");
        if(!roleType.equals("SUPERADMIN"))
        {
            return ResponseClass.responseFailure("Role is not an SuperAdmin");
        }
        List<Admin> adminList = adminRepo.findAll();
        return ResponseClass.responseSuccess("this is all the admin data of the database", "admin", adminList);
    }


    public ResponseEntity<?> deleteAdmin(long adminId) {
        Admin admin = adminRepo.findByAdminId(adminId);
        if(admin!=null){
            adminRepo.delete(admin);
            LoginModel loginModel = loginRepo.findByEmail(admin.getAdminEmail());
            if(loginModel!=null){
                loginRepo.delete(loginModel);
            }
            return ResponseClass.responseSuccess("admin deleted successfully");
        }
        return ResponseClass.responseFailure("admin not found");
    }



}
