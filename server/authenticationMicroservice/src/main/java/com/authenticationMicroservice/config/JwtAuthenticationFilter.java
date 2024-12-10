package com.authenticationMicroservice.config;

import com.authenticationMicroservice.enumerator.UserStatus;
import com.authenticationMicroservice.exception.NifNotFoundException;
import com.authenticationMicroservice.model.UserCredentials;
import com.authenticationMicroservice.service.JwtService;
import com.authenticationMicroservice.service.UserCredentialsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

import static jakarta.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserCredentialsService userCredentialsService;

    public JwtAuthenticationFilter(JwtService jwtService, @Lazy UserCredentialsService userCredentialsService) {
        this.userCredentialsService = userCredentialsService;
        this.jwtService = jwtService;
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String nif = jwtService.extractNif(authHeader.replace("Bearer ", ""));

            if (nif != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserCredentials user = userCredentialsService.findByNif(nif)
                        .orElseThrow(() -> new NifNotFoundException(nif));

                if (user.getStatus() == UserStatus.LOGGED_OUT) {
                    response.setStatus(SC_UNAUTHORIZED);
                    return;
                }

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(user, null, Collections.emptyList());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}