package com.example.main.Configuration;


import com.example.main.Login.Entity.LoginModel;
import com.example.main.Login.Entity.Otp;
import com.example.main.Login.Repo.LoginRepo;
import com.example.main.Login.Repo.OtpRepo;
import com.example.main.Security.JwtService;
import com.example.main.Security.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Log4j2
@Component
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ConfigClass {

    private final JwtUtils jwtUtils;
    private final JavaMailSender javaMailSender;
    private final OtpRepo otpRepo;
    private final LoginRepo loginRepo;
    private final JwtService jwtService;


//    public static final String BASE_URL = "http://localhost:5001/images/";
//    public static final String IMAGE_STORAGE_PATH = "/Users/girjeshbaghel/Documents/Images/";

    public static String IMAGE_STORAGE_PATH = "/mnt/vol1/HMS/HMSImages/";
    public static final String BASE_URL = "https://www.auth.edu2all.in/HMSImages/";

//    public static final long MIN_FILE_SIZE = 10 * 1024; // 10KB
    public static final long MAX_FILE_SIZE = 200 * 1024; // 200KB


    public static String saveImage(MultipartFile image) throws IOException {
        if (!image.isEmpty()) {

                long fileSize = image.getSize();
                if (fileSize > MAX_FILE_SIZE) {
                    throw new IllegalArgumentException("File size must be between 10KB and 200KB.");
                }


            byte[] bytes = image.getBytes();

            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd'T'HHmmss");
            String timestamp = sdf.format(new Date());
            String imageFilename = timestamp + "_" + image.getOriginalFilename();

            String imagePath = IMAGE_STORAGE_PATH.endsWith(File.separator) ? IMAGE_STORAGE_PATH + imageFilename : IMAGE_STORAGE_PATH + File.separator + imageFilename;
            File imageFile = new File(imagePath);
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(bytes);
            }
            // Construct the URL relative to the static resource handler
            return BASE_URL + imageFilename; // Return the URL
        }
        return null;
    }


    public static String saveImage(MultipartFile image,String oldImagePath) throws IOException {
        if (!image.isEmpty()) {
            long fileSize = image.getSize();
            if (fileSize > MAX_FILE_SIZE) {
                throw new IllegalArgumentException("File size must be between "+MAX_FILE_SIZE);
            }
            // Delete the old image if it exists
            if (oldImagePath != null && !oldImagePath.isEmpty()) {
                deleteImage(oldImagePath);
            }
            byte[] bytes = image.getBytes();
            SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd'T'HHmmss");
            String timestamp = sdf.format(new Date());
            String imageFilename = timestamp + "_" + image.getOriginalFilename();

            String imagePath = IMAGE_STORAGE_PATH.endsWith(File.separator) ?
                    IMAGE_STORAGE_PATH + imageFilename :
                    IMAGE_STORAGE_PATH + File.separator + imageFilename;
            File imageFile = new File(imagePath);
            // Save the image to the path
            try (FileOutputStream fos = new FileOutputStream(imageFile)) {
                fos.write(bytes);
            }

            // Construct the URL relative to the static resource handler
            return BASE_URL + imageFilename; // Return the URL
        }
        return null;
    }

    public static boolean deleteImage(String imagePath) {
        if (imagePath != null && !imagePath.isEmpty()) {
            String absolutePath = imagePath.replace(BASE_URL, IMAGE_STORAGE_PATH);
            File file = new File(absolutePath);
            if (file.exists() && file.isFile()) {
                return file.delete();
            }
        }
        return false;
    }


    public HashMap<String, String> getFromToken(String token) {
        token = token.substring(7);
        Claims claim;
        try {
            claim = jwtUtils.extractAllClaims(token);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid Token Format");
        }
        HashMap<String, String> values = new HashMap<String, String>();
        String hotelId = String.valueOf(claim.getSubject());
        String email = String.valueOf(claim.get("email"));
        String roleType = String.valueOf(claim.get("roleType"));
        String userId = String.valueOf(claim.get("userId"));
        values.put("hotelId", hotelId);
        values.put("email", email);
        values.put("roleType", roleType);
        values.put("userId", userId);
        return values;
    }

    public String tokenValue(String token, String value) {
        HashMap<String, String> values = getFromToken(token);
        return values.get(value);
    }

    private Set<String> blockedTokens = new HashSet<>();
    public boolean isTokenBlocked(String token) {
        return blockedTokens.contains(token);
    }

    // Method to block a token
    public void blockToken(String token) {
        blockedTokens.add(token);
    }

    private static String generateId(String prefix, String type) {
        StringBuilder idBuilder = new StringBuilder(prefix);
        if (type != null && !type.isEmpty()) {
            idBuilder.append(type);
        }
        idBuilder.append("-");
        LocalDate currentDate = LocalDate.now();
        String datePart = String.format("%02d%02d", currentDate.getMonthValue(), currentDate.getDayOfMonth());
        idBuilder.append(datePart);
        Random random = new Random();
        int randomNumber = random.nextInt(9000) + 1000; // Generate a random number between 1000 and 9999
        idBuilder.append(randomNumber);
        return idBuilder.toString();
    }

    // Method for generating ID with premix
    public static String idCreate(String premix) {
        return generateId(premix, null);
    }

    // Method for generating ID with hotelId and type
    public static String idCreate(String hotelId, String type) {
        String prefix = hotelId.length() >= 3 ? hotelId.substring(0, 3) : hotelId;
        return generateId(prefix, type);
    }

    public static String generatePassword(int length) {
        final String ALPHA_NUMERIC = "abcdefghijkmnoprstuv0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        String recordUserId;

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(ALPHA_NUMERIC.length());
            char randomChar = ALPHA_NUMERIC.charAt(randomIndex);
            sb.append(randomChar);
        }
        recordUserId = sb.toString();
        return recordUserId;
    }


    private void configureMailSender() {
        if(javaMailSender instanceof JavaMailSenderImpl) {
            JavaMailSenderImpl mailSenderImpl = (JavaMailSenderImpl) javaMailSender;

            Properties mailProperties = new Properties();
            mailProperties.setProperty("mail.smtp.starttls.enable", "true");
            mailProperties.setProperty("mail.smtp.ssl.trust", "smtp.gmail.com"); // Replace with your mail server hostname

            mailSenderImpl.setJavaMailProperties(mailProperties);
        }
    }

    public String getOtpSetUser(String email, String schoolId, String subject, String body, int otp, String roleType, String uniqueId) {
        try {
            List<Otp> old = otpRepo.findAllByEmail(email);
            System.out.println("test this because it can cause an error");
            otpRepo.deleteAll(old);
            System.out.println("old OTP deleted");
        } catch (Exception e) {
            System.out.println("not found in OTP Table");
        }

        LocalDateTime issued = LocalDateTime.now();
        LocalDateTime expiry = issued.plusSeconds(600);

        Otp otpSent = new Otp();
        otpSent.setOtp(otp);
        otpSent.setEmail(email);
        otpSent.setIssue(issued);
        otpSent.setExpires(expiry);

        // OTP SMTP method Call
        sendEmail(email, subject, body);
        otpRepo.save(otpSent);
        LoginModel user = loginRepo.getReferenceByEmail(email);
        if(user == null) {
            LoginModel newuser = new LoginModel();
            System.out.println("is schoolId coming null" + schoolId);
            newuser.setHotelId(schoolId);
            newuser.setRole(roleType);
            newuser.setEmail(email);
            System.out.println("checking if it is null " + uniqueId);
            newuser.setUserId(uniqueId);
            loginRepo.save(newuser);
            return jwtService.generateToken("None", email, roleType, "None", false);
        }
        return jwtService.generateToken("None", email, roleType, "None", false);
    }

    public String generateString(int length) {
        // Set of characters to choose from
        String alphabet = "abcdefghijklmnopqrstuvwxyz";

        // Create a StringBuilder to store the random string
        StringBuilder randomString = new StringBuilder();

        // Create a Random object
        Random random = new Random();

        // Generate the random string
        for(int i = 0; i < length; i++) {
            // Get a random index from the alphabet
            int index = random.nextInt(alphabet.length());

            // Append the randomly selected character to the StringBuilder
            randomString.append(alphabet.charAt(index));
        }

        // Print the generated random string
        return randomString.toString();
    }


    @Async
    public CompletableFuture<Boolean> sendEmail(String toMailId, String subject, String body) {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(toMailId);
            helper.setSubject(subject);
            helper.setText(body);
            configureMailSender();

            javaMailSender.send(message);
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("SMTP Failed for EmailId: {}", toMailId);
            return CompletableFuture.completedFuture(false);
        }
    }



    public static List<Object> getDateTimeWeekDayFromInstant(Instant currentInstant){
        if(currentInstant == null){ currentInstant = Instant.now(); }
        ZoneId currentZone = ZoneId.systemDefault();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a");
        String formattedTime = currentInstant.atZone(currentZone).format(formatter);
        DateTimeFormatter dayFormatter = DateTimeFormatter.ofPattern("EEEE");
        String dayOfWeek = currentInstant.atZone(currentZone).format(dayFormatter);
        return Arrays.asList(formattedTime, dayOfWeek);
    }

    private static final int DEFAULT_PAGE_NUMBER = 1;
    private static final int DEFAULT_PAGE_SIZE = 10;

    public PageRequest getPageRequest(Integer page, Integer size) {
        int pageNumber = (page == null || page < 1) ? DEFAULT_PAGE_NUMBER - 1 : page - 1;
        int pageSize = size == null || size < 1 ? DEFAULT_PAGE_SIZE : size;
        return PageRequest.of(pageNumber, pageSize);
    }

    public String getNextPageUrl(HttpServletRequest request, int nextPage, int size) {
        String requestUrl = request.getRequestURL().toString();
        String queryString = request.getQueryString();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(requestUrl)
                .replaceQueryParam("page", nextPage)
                .replaceQueryParam("size", size);

        if (queryString != null) {
            builder.replaceQuery(queryString);
        }

        return builder.toUriString();
    }



}
