package com.portalMicroservice.client.authentication;

import com.portalMicroservice.config.CustomErrorDecoder;
import com.portalMicroservice.dto.authentication.AuthenticationResponseDTO;
import com.portalMicroservice.dto.authentication.SignInRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "authenticationFeignClient", url = "${authentication-microservice-authorization.url}", configuration = CustomErrorDecoder.class)
public interface AuthenticationFeignClient {

    @PostMapping("/sign-in")
    ResponseEntity<AuthenticationResponseDTO> signIn(@RequestBody SignInRequestDTO signInRequestDTO);

    @PostMapping("/refresh-token")
    ResponseEntity<AuthenticationResponseDTO> refreshToken(@RequestHeader("Authorization") String authHeader);
}