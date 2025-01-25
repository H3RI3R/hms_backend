package com.example.main.Login.Controller;

import com.example.main.Configuration.ConfigClass;
import com.example.main.Login.DTO.LoginDTO;
import com.example.main.Login.Service.LoginService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class LoginController {



    private final LoginService loginService;
    private final ConfigClass configClass;

    @PostMapping("/all")
    public ResponseEntity<HashMap<String, String>> loginUser(
            @RequestBody LoginDTO loginDetail, HttpServletRequest request) {
        HashMap<String,String> responseBody = loginService.loginGenerateToken(loginDetail,request);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String token) {
        return loginService.logoutUser(token);
    }


    @PostMapping("/changeMyPassword")
    public ResponseEntity<?> justChangePassword(
            @RequestParam String  email,
            @RequestParam String password)
    {
        return loginService.changePassword(email,password);
    }

    @PostMapping("/getOtp")
    public ResponseEntity<?> setPassword(
            @RequestParam("email") String email
    ){
        if(!email.isEmpty()){
            System.out.println(email);
            System.out.println("OTP req received");
            HashMap<String, String> updated = loginService.sendOtpAndIssueToken(email);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.ok("email not received");
    }


    @PostMapping("/setPassword")
    public ResponseEntity<HashMap<String, String>> passwordChange(
            @RequestHeader("Authorization") String token,
            @RequestParam("password") String password) throws BadRequestException {
        String email = configClass.tokenValue(token, "email");
        HashMap<String, String> resp = new HashMap<>();
        System.out.println("Request Received: " +email);
        System.out.println("password is received: ");
        boolean verified = loginService.updatePassword(password, email);

        if(verified){
            resp.put("status","success");
            resp.put("msg", "Password changed");
            return ResponseEntity.ok(resp);
        }
        resp.put("status","failed");
        resp.put("msg", "password invalid");
        return ResponseEntity.ok(resp);
    }


    @PostMapping("/verify-otp")
    public ResponseEntity<?> verifyOtp(
            @RequestParam(required = false,name = "OTP") int otp,
            @RequestHeader("Authorization") String token
    )
    {
        return loginService.generatePassword(token, otp);
    }
}
