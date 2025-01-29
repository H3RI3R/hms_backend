package com.example.main.StaffManagement.Service;


import com.example.main.Exception.ResponseClass;
import com.example.main.Login.DTO.LoginDTO;
import com.example.main.Login.Entity.LoginModel;
import com.example.main.Login.Repo.LoginRepo;
import com.example.main.Login.Service.LoginService;
import com.example.main.StaffManagement.DTO.StaffDTO;
import com.example.main.StaffManagement.Model.Role;
import com.example.main.StaffManagement.Model.Staff;
import com.example.main.StaffManagement.Repository.RoleRepo;
import com.example.main.StaffManagement.Repository.StaffRepo;
import com.example.main.Support.DateTimeWeekDay;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StaffService {
    private final StaffRepo staffRepo;
    private final RoleRepo roleRepo;
    private final PasswordEncoder passwordEncoder;
    private final LoginRepo loginRepo;


    public ResponseEntity<?> addNewStaff(String hotelId, StaffDTO staff1) {
        Role role = roleRepo.findById(staff1.getRoleId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "role not found"));
        Staff staff = new Staff();
        staff.setHotelId(hotelId);
        staff.setName(staff1.getName());
        staff.setUserName(staff1.getUserName());
        staff.setEmail(staff1.getEmail());
        staff.setRole(role);
        staff.setAddress(staff1.getAddress());
        staff.setCreatedAt(Instant.now());
        staffRepo.save(staff);

        LoginModel loginModel = loginRepo.findByEmail(staff1.getEmail());
        if(loginModel!=null){
            return ResponseClass.responseFailure("this email is already exit in login table");
        }
        LoginModel login = new LoginModel();
        login.setEmail(staff1.getEmail());
        login.setRole("STAFF");
        login.setUserName(staff1.getUserName());
        login.setHotelId(hotelId);
        login.setPassword(passwordEncoder.encode(staff1.getPassword()));
        loginRepo.save(login);
        return ResponseClass.responseSuccess("new staff created");
    }

    public ResponseEntity<?> updateStaff(String hotelId, Long id, String name, String username, String email,String address, Long roleId, String password) {
        Staff staff1 = staffRepo.findByIdAndHotelId(id,hotelId);
        staff1.setName(name!=null ? name : staff1.getName());
        LoginModel loginModel = loginRepo.findByEmail(email);
        if(loginModel!=null){
            return ResponseClass.responseFailure("this email is already exit in login table");
        }
        staff1.setEmail(email!=null ? email : staff1.getEmail());

        staff1.setUserName(username!=null ? username : staff1.getUserName());
        if(password!=null){
            LoginModel login = loginRepo.findByEmail(staff1.getEmail());
            login.setPassword(passwordEncoder.encode(password));
            loginRepo.save(login);
        }
        if(roleId!=null){
            Role role = roleRepo.findById(roleId).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "role not found"));
            staff1.setRole(role);
        }
        if(address!=null)
        {
            staff1.setAddress(address);
        }
        staffRepo.save(staff1);
        return ResponseClass.responseSuccess("staff updated");
    }

    public ResponseEntity<?> getOneStaff(String hotelId, Long id) {
        Staff staff = staffRepo.findByIdAndHotelId(id, hotelId);
        if(staff==null){ throw new ResponseStatusException(HttpStatus.NOT_FOUND, "staff not found"); }
        StaffDTORecord staff1 = new StaffDTORecord(
                staff.getId(),
                staff.getHotelId(),
                staff.getName(),
                staff.getUserName(),
                staff.getEmail(),
                staff.getRole(),
                staff.getStatus(),
                new DateTimeWeekDay(staff.getCreatedAt() == null ? Instant.now() : staff.getCreatedAt()));
        return ResponseClass.responseSuccess("staff found", "staff", staff1);
    }

    public ResponseEntity<?> getAllStaff(String hotelId) {
        List<Staff> staffs = staffRepo.findAllByHotelId(hotelId);
        if(staffs.isEmpty()){ throw new ResponseStatusException(HttpStatus.NOT_FOUND, "staffs not found"); }
        List<StaffDTORecord> staffs1 = staffs.stream().map(staff -> new StaffDTORecord(
                staff.getId(),
                staff.getHotelId(),
                staff.getName(),
                staff.getUserName(),
                staff.getEmail(),
                staff.getRole(),
                (staff.getStatus() == null ? "ENABLED" : staff.getStatus()),
                new DateTimeWeekDay(staff.getCreatedAt() == null ? Instant.now() : staff.getCreatedAt()))).toList();
        return ResponseClass.responseSuccess("staffs found", "staffs", staffs1);
    }

    public ResponseEntity<?> deleteStaff(String hotelId, Long id) {
        Staff staff = staffRepo.findByIdAndHotelId(id, hotelId);
        Role role = staff.getRole();
        role.getStaffs().remove(staff);
        roleRepo.save(role);
        staff.setRole(null);
        staffRepo.delete(staff);
        return ResponseClass.responseSuccess("staff deleted");
    }

    public ResponseEntity<?> banStaff(String hotelId, Long id) {
        Staff staff = staffRepo.findByIdAndHotelId(id, hotelId);
//        System.out.println(staff.getEmail());
        LoginModel login = loginRepo.findByEmail(staff.getEmail());
        if(login==null){
            LoginModel userLogin =  new LoginModel();
            userLogin.setEmail(staff.getEmail());
            userLogin.setRole("STAFF");
            userLogin.setHotelId(hotelId);
            userLogin.setIsActive(true);
            loginRepo.save(userLogin);
            login = userLogin;
        }

        if(staff.getStatus()==null){
            staff.setStatus("ENABLED");
        }
        if(staff.getStatus().equals("ENABLED")){
            staff.setStatus("DISABLED");
            login.setIsActive(false);
            loginRepo.save(login);
        }else {
            staff.setStatus("ENABLED");
            login.setIsActive(true);
            loginRepo.save(login);
        }
        staffRepo.save(staff);
        return ResponseClass.responseSuccess("staff banned");
    }

    private final LoginService loginService;
    public HashMap<String, String> loginStaff(String hotelId, Long staffId, HttpServletRequest request) {
        Staff staff = staffRepo.findByIdAndHotelId(staffId, hotelId);
        LoginModel  login = loginRepo.findByEmail(staff.getEmail());
        LoginDTO loginDetails = new LoginDTO();
        loginDetails.setEmail(login.getEmail());
        loginDetails.setPassword(login.getPassword());
        loginDetails.setRememberMe(false);
        return loginService.loginGenerateToken(loginDetails,request);
    }

    public ResponseEntity<?> getBannedGuest(String hotelId) {
        List<Staff>  staff = staffRepo.findAllByHotelIdAndStatus(hotelId,"DISABLED");
        return ResponseClass.responseSuccess("All Banned Staff","bannedStaff",staff);
    }


    record StaffDTORecord(
            Long id,
            String hotelId,
            String name,
            String userName,
            String email,
            Role role,
            String status,
            DateTimeWeekDay createdAt
    ){}
}
