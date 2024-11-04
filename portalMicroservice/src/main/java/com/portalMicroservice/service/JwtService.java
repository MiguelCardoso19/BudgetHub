package com.portalMicroservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${app.security.token.secret}")
    private String SECRET_KEY;

    public boolean isTokenValid(Claims claims, String expectedNif) {
        String nif = claims.getSubject();
        return nif.equals(expectedNif) && !isTokenExpired(claims);
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }
}
