//package com.portalMicroservice.config;
//
//import com.portalMicroservice.service.JwtService;
//import com.portalMicroservice.util.JwtAuthenticationFilterUtils;
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
//import static com.portalMicroservice.exception.ErrorMessage.INVALID_AUTHORIZATION_HEADER;
//import static com.portalMicroservice.exception.ErrorMessage.INVALID_TOKEN;
//
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final JwtService jwtTokenService;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String authHeader = request.getHeader("Authorization");
//
//        if (JwtAuthenticationFilterUtils.isExemptEndpoint(request.getRequestURI())) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            JwtAuthenticationFilterUtils.writeErrorResponse(response,
//                    INVALID_AUTHORIZATION_HEADER.getErrorCode(),
//                    INVALID_AUTHORIZATION_HEADER.getMessage(),
//                    INVALID_AUTHORIZATION_HEADER.getStatus().value());
//            return;
//        }
//
//        Claims claims = jwtTokenService.parseToken(authHeader.replace("Bearer ", ""));
//
//        if (!jwtTokenService.isTokenValid(claims, request.getHeader("Nif"))) {
//            JwtAuthenticationFilterUtils.writeErrorResponse(response,
//                    INVALID_TOKEN.getErrorCode(),
//                    INVALID_TOKEN.getMessage(),
//                    INVALID_TOKEN.getStatus().value());
//            return;
//        }
//
//        JwtAuthenticationFilterUtils.setUpSpringAuthentication(claims);
//        filterChain.doFilter(request, response);
//    }
//}