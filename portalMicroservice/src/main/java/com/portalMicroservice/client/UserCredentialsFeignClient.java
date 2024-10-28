package com.portalMicroservice.client;

import com.portalMicroservice.config.CustomErrorDecoder;
import com.portalMicroservice.dto.authentication.AuthenticationResponseDTO;
import com.portalMicroservice.dto.authentication.DeleteRequestDTO;
import com.portalMicroservice.dto.authentication.UserCredentialsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "userCredentialsFeignClient", url = "${authentication-microservice-user-credentials.url}", configuration = CustomErrorDecoder.class)
public interface UserCredentialsFeignClient {

    @PostMapping("/register")
    ResponseEntity<AuthenticationResponseDTO> register(@RequestBody UserCredentialsDTO userCredentialsDTO);

    @PutMapping("/update")
    ResponseEntity<UserCredentialsDTO> update(@RequestBody UserCredentialsDTO userCredentialsDTO);

    @DeleteMapping("/delete")
    ResponseEntity<Void> delete(@RequestBody DeleteRequestDTO deleteRequestDTO);
}