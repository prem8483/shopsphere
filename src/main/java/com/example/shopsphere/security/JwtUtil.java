package com.example.shopsphere.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
    
    private final SecretKey SECRET_KEY = Keys.hmacShaKeyFor(
        "mysecretkeymysecretkeymysecretkey12".getBytes()
    );

    public String generateToken ( String email ) throws Exception {
        return Jwts.builder()
                    .setSubject(email)
                    .setIssuedAt(new Date())
                    .setExpiration( new Date( System.currentTimeMillis( ) * 1000 * 60 * 60 ) )
                    .signWith(SECRET_KEY)
                    .compact();
    }

    public String extractEmail( String token ) {
        return Jwts.parserBuilder()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
    };

    public boolean validateToken( String token, String email ) {
        String extractedEmail = extractEmail(token);
        return extractedEmail.equals(email) && !isTokenExpired(token);
    }

    public boolean isTokenExpired( String token ) {
        Date expiration = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();

        return expiration.before(new Date());
    }
}
