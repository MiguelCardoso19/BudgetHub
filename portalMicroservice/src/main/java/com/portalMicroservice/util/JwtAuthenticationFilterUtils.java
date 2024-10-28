package com.portalMicroservice.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.portalMicroservice.exception.ErrorResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilterUtils {
    private static final ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

    public static boolean isExemptEndpoint(String requestURI) {
        return requestURI.startsWith("/api/v1/auth/sign-in") ||
                requestURI.startsWith("/api/v1/user-credentials/register") ||
                requestURI.startsWith("/v3/api-docs") ||
                requestURI.startsWith("/swagger-ui") ||
                requestURI.startsWith("/swagger-resources") ||
                requestURI.startsWith("/webjars/springfox-swagger-ui");
    }

    public static void setUpSpringAuthentication(Claims claims) {
        List<SimpleGrantedAuthority> authorities = extractAuthoritiesFromClaims(claims);

        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    public static void writeErrorResponse(HttpServletResponse response, String code, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.getWriter().write(objectWriter.writeValueAsString(new ErrorResponse(code, message, status)));
        response.getWriter().flush();
    }

    private static List<SimpleGrantedAuthority> extractAuthoritiesFromClaims(Claims claims) {
        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}
