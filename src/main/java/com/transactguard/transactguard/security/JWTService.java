package com.transactguard.transactguard.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Service
public class JWTService {
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(Map<String, Object> extraClaims, String username) {

        long EXPIRATION_TIME = 1000 * 60 * 60;
        Date TOKEN_EXPIRATION = new Date(System.currentTimeMillis() + EXPIRATION_TIME);

        return Jwts.builder()
                .claims(extraClaims)
                .subject(username)
                .issuedAt(new Date())
                .expiration(TOKEN_EXPIRATION)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUserName(String token) {
        Claims claimsMap = extractAllClaims(token);
        return claimsMap.getSubject();
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}
