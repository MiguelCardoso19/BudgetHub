package com.authenticationMicroservice.service;

import com.authenticationMicroservice.enumerator.UserRoleEnum;
import com.authenticationMicroservice.model.UserCredentials;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.lang.reflect.Field;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.MockitoAnnotations.openMocks;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    private final String SECRET_KEY = "testSecretKey";

    private UserCredentials user = new UserCredentials();

    @BeforeEach
    void setUp() throws Exception {
        openMocks(this);
        user.setEmail("test@example.com");
        user.setNif("fake");
        user.setRoles(Collections.singleton(UserRoleEnum.ADMIN));

        setPrivateField(jwtService, "SECRET_KEY", SECRET_KEY);
        setPrivateField(jwtService, "JWT_EXPIRATION", 1000 * 60 * 60);
        setPrivateField(jwtService, "JWT_REFRESH_TOKEN_EXPIRATION", 1000 * 60 * 60 * 24 * 7);
        setPrivateField(jwtService, "JWT_RESET_PASSWORD_EXPIRATION", 1000 * 60 * 15);
    }

    @Test
    void testGenerateToken() {
        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertTrue(isValidJwt(token));
        String nif = jwtService.extractNif(token);
        assertEquals(user.getNif(), nif);
    }

    @Test
    void testGenerateRefreshToken() {
        String token = jwtService.generateRefreshToken(user);

        assertNotNull(token);
        assertTrue(isValidJwt(token));
        String nif = jwtService.extractNif(token);
        assertEquals(user.getNif(), nif);
    }

    @Test
    void testGenerateResetPasswordToken() {
        String token = jwtService.generateResetPasswordToken(user);

        assertNotNull(token);
        assertTrue(isValidJwt(token));
        String nif = jwtService.extractNif(token);
        assertEquals(user.getNif(), nif);
    }

    @Test
    void testExtractNif() {
        String token = jwtService.generateToken(user);

        String extractedNif = jwtService.extractNif(token);

        assertEquals(user.getNif(), extractedNif);
    }

    @Test
    void testIsTokenValid_ValidToken() {
        String token = jwtService.generateToken(user);
        String authHeader = "Bearer " + token;

        boolean isValid = jwtService.isTokenValid(authHeader, user.getNif());

        assertTrue(isValid);
    }

    @Test
    void testIsTokenValid_InvalidToken() {
        String invalidToken = "invalid.token.structure";
        String authHeader = "Bearer " + invalidToken;

        boolean isValid;
        try {
            isValid = jwtService.isTokenValid(authHeader, user.getNif());
        } catch (Exception e) {
            isValid = false;
        }

        assertFalse(isValid);
    }

    @Test
    void testIsTokenValid_ExpiredToken() {
        String expiredToken = createToken(user, -1000 * 60);
        String authHeader = "Bearer " + expiredToken;

        boolean isValid;
        try {
            isValid = jwtService.isTokenValid(authHeader, user.getNif());
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            isValid = false;
        }

        assertFalse(isValid);
    }

    private String createToken(UserCredentials user, long expirationTime) {
        return Jwts.builder()
                .setSubject(user.getNif())
                .claim("roles", user.getRoles().stream().map(Enum::name).toList())
                .claim("email", user.getEmail())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    private boolean isValidJwt(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
