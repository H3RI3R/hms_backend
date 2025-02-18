package com.example.main.Login.Service;


import com.example.main.Configuration.ConfigClass;
import com.example.main.Exception.ResponseClass;
import com.example.main.Login.DTO.LoginDTO;
import com.example.main.Login.Entity.LoginModel;
import com.example.main.Login.Entity.Otp;
import com.example.main.Login.Repo.LoginRepo;
import com.example.main.Login.Repo.OtpRepo;
import com.example.main.Report.Entity.NotificationHistory;
import com.example.main.Report.Repo.NotificationHistoryRepository;
import com.example.main.Security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Log4j2
@Service
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LoginService {

    private final LoginRepo loginRepo;
    private final JwtService jwtService;
    private final ConfigClass configClass;
    private final OtpRepo otpRepo;
    private final PasswordEncoder passwordEncoder;
@Autowired
    private NotificationHistoryRepository notificationHistoryRepository;



    public HashMap<String, String> loginGenerateToken(LoginDTO loginDetails,HttpServletRequest request){
        HashMap<String,String> response =  new HashMap<>();
        LoginModel loginInfo = loginRepo.findByEmail(loginDetails.getEmail());
        if (loginInfo != null) {
            if(loginInfo.getIsActive() != null && loginInfo.getIsActive().equals(false)){
                response.put("status", "failed");
                response.put("message", "user has been banned");
                return response;
            }
            String userPassword = loginInfo.getPassword();
            if(loginDetails.getLat() == null && loginDetails.getLog() == null){
                response.put("status","failed");
                response.put("message","Lat and log not provided by saqib .");
                return response;
            }
            if(passwordEncoder.matches(loginDetails.getPassword(), userPassword)) {
                String token;
                if(loginDetails.isRememberMe()){
                    token =  jwtService.generateToken(loginInfo.getHotelId(), loginInfo.getEmail(), loginInfo.getRole(), loginInfo.getUserId(), true);
                }else {
                    token =  jwtService.generateToken(loginInfo.getHotelId(), loginInfo.getEmail(), loginInfo.getRole(), loginInfo.getUserId(), false);
                }


                String browser = getBrowserInfo(request);
                String systemIP = getClientIP(request);
                loginInfo.setLat(loginDetails.getLat());
                loginInfo.setLog(loginDetails.getLog());
                loginInfo.setSystemIP(systemIP);
                loginInfo.setLocation("NOIDA");
                loginInfo.setBrowser(browser);
                loginInfo.setLoginAt(LocalDateTime.now());
                loginRepo.save(loginInfo);
                response.put("token", token);
                response.put("system IP",systemIP);
                response.put("system browser",browser);
                response.put("status","success");
                response.put("message", "user logged in");
                response.put("roleType", loginInfo.getRole());
                response.put("name", loginInfo.getUserName());
                response.put("email", loginInfo.getEmail());
                log.info("login user info: {} {} {}", loginInfo.getHotelId(), loginInfo.getUserId(), loginInfo.getEmail());
                return response;
            }

            response.put("status","failed");
            response.put("message", "Invalid Password");
            return response;
        }
        response.put("status", "failed");
        response.put("message", "Invalid EmailId");
        return  response;
    }


    private String getClientIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
    public String getBrowserInfo(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        if (userAgent == null) {
            return "Unknown Browser";
        }
        if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            return "Internet Explorer";
        } else {
            return "Other";
        }
    }

    public ResponseEntity<?> logoutUser(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String authToken = token.substring(7);
            if (!configClass.isTokenBlocked(authToken)) {
                configClass.blockToken(authToken);
                return ResponseClass.responseSuccess("logout successful");
            }
        }
        return ResponseClass.responseFailure("already logout successful");
    }


    public ResponseEntity<?> changePassword(String email, String password) {
        LoginModel  user = loginRepo.findByEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        loginRepo.save(user);
        return ResponseClass.responseSuccess("password changed");
    }

    public HashMap<String, String> sendOtpAndIssueToken(String email){
        Random random = new Random();
        int otp = random.nextInt(1001, 9999);
        String body = "This is your OTP : "+ otp;
        String subject = "OTP For Password Reset";

        String token;
        HashMap<String,String> response = new HashMap<>();
        try{
            otpRepo.deleteAllByEmail(email);
            System.out.println("old OTP deleted");
        }catch (Exception e){
            System.out.println("not found in OTP Table");
        }

        LoginModel loginUser = loginRepo.findByEmail(email);
        if(loginUser != null){
            token = configClass.getOtpSetUser(email, loginUser.getHotelId(), subject, body, otp, loginUser.getRole(), loginUser.getUserId());
            if(!token.equals("notSent")){
                NotificationHistory notification = new NotificationHistory();
                notification.setUserName(loginUser.getUserName());
                notification.setUserEmail(email);
                notification.setDateTime(LocalDateTime.now());
                notification.setSender("Email");
                notification.setSubject(subject);
                notification.setMessage(body);

                notificationHistoryRepository.save(notification);

                response.put("token", token);
                response.put("status","success");
                response.put("message", "OTP Sent to User");
                log.info("Login User data: userId: {} {}", loginUser.getUserId(), loginUser.getHotelId());
                return response;
            }else {
                response.put("token", "None");
                response.put("status", "failed");
                response.put("message", "Unable to send OTP");
                return response;
            }
        }
        response.put("token","None");
        response.put("status","failed");
        response.put("message", "User Not Found");
        return response;

    }

    public boolean updatePassword(String password, String email) throws BadRequestException {
        LoginModel user = loginRepo.findByEmail(email);
        if(user != null){
            user.setPassword(passwordEncoder.encode(password));
            loginRepo.save(user);
            return true;
        }else {
            throw new BadRequestException("User not found");
        }
    }


    public boolean verifyOtp(String email, int otp) {
        Otp curOtp = otpRepo.findByEmail(email);
        log.info("OPT Verification- from user: {} & from DB: {}",otp, curOtp.getOtp() );
        return curOtp.getOtp()==otp;
    }
    @Transactional
    public ResponseEntity<?> generatePassword(String token, int otp_mobile) {
        if (token.isEmpty()){
            ResponseClass.responseFailure("Bearer token not present");
        }
        String email = configClass.tokenValue(token, "email");

        boolean verified = verifyOtp(email, otp_mobile);
        if(verified){
            String password = configClass.generateString(10);
            LoginModel userLogin = loginRepo.findByEmail(email);
            userLogin.setPassword(passwordEncoder.encode(password));
            loginRepo.save(userLogin);
            configClass.sendEmail(email,
                    "new password",
                    "This is your newly generated password: "+password+"\nYou can change it from inside the setting penal of application.");
            return ResponseClass.responseSuccess("password sent to email");
        }
        return ResponseClass.responseFailure("invalid otp");
    }
}
