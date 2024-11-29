package com.example.SpringAI.Configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "UDDIN SIYAM",
                        email = "siyamuddin177gmail.com",
                        url = "https://siyamuddin.netlify.app"
                ),
                description = "This a student assistant application which help students to manage their classes with the assistance of AI.",
                title = "AiBuddy",
                version = "1.0",
                termsOfService = "Terms of Service"
        ),
        servers ={
                @Server(
                        description = "Local Dev",
                        url = "http://localhost:8080/"
                )
        }
)
public class OpenAPiConfiguration {
}
