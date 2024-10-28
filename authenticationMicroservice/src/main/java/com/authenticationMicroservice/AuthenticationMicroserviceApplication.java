package com.authenticationMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class AuthenticationMicroserviceApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthenticationMicroserviceApplication.class, args);
    }

}