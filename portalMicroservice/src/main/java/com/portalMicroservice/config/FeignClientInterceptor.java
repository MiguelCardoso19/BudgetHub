package com.portalMicroservice.config;

import com.portalMicroservice.util.FeignClientUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@Component
public class FeignClientInterceptor implements RequestInterceptor {

    @SneakyThrows
    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            String authHeader = attributes.getRequest().getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                requestTemplate.header("Authorization", authHeader);
            }

            if ("application/json".equalsIgnoreCase(requestTemplate.headers().getOrDefault("Content-Type",
                    List.of()).stream().findFirst().orElse(null))) {
                String body = new String(requestTemplate.body(), UTF_8);
                FeignClientUtils.extractUserDetails(body, attributes);
            }
        }
    }
}