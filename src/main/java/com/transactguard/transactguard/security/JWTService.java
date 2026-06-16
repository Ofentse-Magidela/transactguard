package com.transactguard.transactguard.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JWTService {
    private final SecretKey SECRET_KEY = Jwts.SIG.HS256.key().build();

    public String generateToken(Map<String, Object> extraClaims, String username) {

        long EXPIRATION_TIME = 1000 * 60 * 60;
        Date TOKEN_EXPIRATION = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(TOKEN_EXPIRATION)
                .signWith(SECRET_KEY)
                .compact();
    }

    public String extractUserName(String token) {
        Claims claimsMap = extractAllClaims(token);
        return claimsMap.getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
