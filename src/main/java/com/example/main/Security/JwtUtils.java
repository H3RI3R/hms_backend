package com.example.main.Security;



import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {


    // JwtService and JwtUtils classes
    private static final String secret = "0ru239ry28fh2bf82f382382098302jf9nc20290fueoijvoe409nw439h384gun39ng398h39jg394j3mc3j9fj3";

    // Base64 decode the secret and use it for signing/verification
    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }



    public void validateToken(final  String token){
        Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token);

    }



    public Claims extractAllClaims(String token){
            return Jwts.parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

    }


    public boolean isTokenExpired(String token){
        return this.extractAllClaims(token).getExpiration().before(new Date());
    }



}
