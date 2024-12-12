package com.portalMicroservice.client.authentication;

import com.portalMicroservice.config.CustomErrorDecoder;
import com.portalMicroservice.dto.authentication.AuthenticationResponseDTO;
import com.portalMicroservice.dto.authentication.DeleteRequestDTO;
import com.portalMicroservice.dto.authentication.ResetPasswordRequestDTO;
import com.portalMicroservice.dto.authentication.UserCredentialsDTO;
import com.portalMicroservice.enumerator.UserStatus;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "userCredentialsFeignClient", url = "${authentication-microservice-user-credentials.url}", configuration = CustomErrorDecoder.class)
public interface UserCredentialsFeignClient {

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponseDTO> register(@RequestBody UserCredentialsDTO userCredentialsDTO);

    @PutMapping("/update")
    ResponseEntity<UserCredentialsDTO> update(@RequestBody UserCredentialsDTO userCredentialsDTO);

    @DeleteMapping("/delete")
    ResponseEntity<Void> delete(@RequestBody DeleteRequestDTO deleteRequestDTO);

    @PostMapping("/recover-password")
    ResponseEntity<Void> recoverPassword(@RequestParam("email") String email);

    @PostMapping("/reset-password")
    ResponseEntity<Void> resetPassword(@RequestBody ResetPasswordRequestDTO resetPasswordRequestDTO);

    @GetMapping("/status")
    UserStatus getUserStatus(@RequestParam("nif") String nif);

    @GetMapping("/get-user-by-id")
    ResponseEntity<UserCredentialsDTO> getUserById(@RequestParam("id") UUID id);
}