package com.notificationMicroservice.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Miguel Cardoso",
                        email = "miguel40cardoso@gmail.com",
                        url = "https://www.linkedin.com/in/miguelcardoso19/"
                ),
                description = "OpenApi documentation for Notification Microservice",
                title = "OpenApi specification - Notification Microservice",
                version = "1.0"
        ),
        servers = {
                @Server(
                        description = "Local env",
                        url = "http://localhost:8080"
                )
        }
)
public class OpenApiConfig {
}
