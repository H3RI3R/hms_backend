package com.example.main.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;



import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService
{


    // JwtService and JwtUtils classes
    private static final String secret = "0ru239ry28fh2bf82f382382098302jf9nc20290fueoijvoe409nw439h384gun39ng398h39jg394j3mc3j9fj3";

    // Base64 decode the secret and use it for signing/verification


    private static final int hour_12 = 43200;
    private static final int days_30 = 2592000;


    public String generateToken(String hotelId, String email, String roleType, String userId, boolean rememberMe){
     //   System.out.println("HotelId :"+hotelId + "RoleType :"+roleType+ "UserId :"+userId+ "email :"+email);
        Claims claims = Jwts.claims().setSubject(hotelId);
        claims.put("userId", userId);
        claims.put("email",email);

        System.out.println("Token Email: "+email);
        claims.put("roleType",roleType);
        if(rememberMe){
            return createToken(claims, days_30);
        }
        return createToken(claims, hour_12);
    }


    private String createToken(Map<String, Object> claims, int timer){

        Instant issued = Instant.now();
        Instant expiry = issued.plusSeconds(timer);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(issued))
                .setExpiration(Date.from(expiry))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

