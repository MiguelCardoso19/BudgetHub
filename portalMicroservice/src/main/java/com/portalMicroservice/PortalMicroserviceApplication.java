package com.portalMicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PortalMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PortalMicroserviceApplication.class, args);
	}
}