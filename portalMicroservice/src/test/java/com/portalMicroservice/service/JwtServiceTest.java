package com.portalMicroservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS256;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private HttpServletRequest request;

    private final String SECRET_KEY = "testSecretKey";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        openMocks(this);
        mockRequestContextHolder(request);
        setPrivateField(jwtService, "SECRET_KEY", SECRET_KEY);
    }

    @Test
    void testIsTokenValid_Success() {
        Claims claims = Jwts.claims().setSubject("testNif");
        claims.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        assertTrue(jwtService.isTokenValid(claims, "testNif"));
    }

    @Test
    void testIsTokenValid_InvalidToken() {
        Claims claims = Jwts.claims().setSubject("testNif");
        claims.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60));

        assertFalse(jwtService.isTokenValid(claims, "wrongNif"));
    }

    @Test
    void testIsTokenValid_ExpiredToken() {
        Claims claims = Jwts.claims().setSubject("testNif");
        claims.setExpiration(new Date(System.currentTimeMillis() - 1000 * 60));

        assertFalse(jwtService.isTokenValid(claims, "testNif"));
    }

    @Test
    void testParseToken_Success() {
        String token = Jwts.builder()
                .setSubject("testNif")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(HS256, SECRET_KEY)
                .compact();

        Claims claims = jwtService.parseToken(token);

        assertEquals("testNif", claims.getSubject());
    }

    @Test
    void testGetEmailFromRequest_WithToken() {
        String token = Jwts.builder()
                .setSubject("testNif")
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .claim("email", "test@example.com")
                .signWith(HS256, SECRET_KEY)
                .compact();

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        assertEquals("test@example.com", jwtService.getEmailFromRequest());
    }

    @Test
    void testGetEmailFromRequest_NoToken() {
        when(request.getHeader("Authorization")).thenReturn(null);

        assertNull(jwtService.getEmailFromRequest());
    }

    private void mockRequestContextHolder(HttpServletRequest request) {
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        var field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}