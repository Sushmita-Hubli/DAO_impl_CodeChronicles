package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;

@Configuration
public class SwaggerConfig {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    @Bean
    public OpenAPI myCustomConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("CodeChronicles – Scalable Platform for Developer-Focused Content Management") 
                        .description("A RESTful API for Content Management. Developed by Sushmita Hubli.") 
                        .version("1.0") 
                        .contact(new Contact()
                                .name("Sushmita Hubli") 
                                .email("hublisushmita7@gmail.com") 
                                .url("https://github.com/Sushmita-Hubli") 
                        )
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html") 
                        )
                )
                // Apply security globally
                .addSecurityItem(new SecurityRequirement().addList("JWT"))
                .components(new Components()
                        .addSecuritySchemes("JWT", new SecurityScheme()
                                .name("JWT")
                                .type(SecurityScheme.Type.HTTP) 
                                .scheme("bearer")
                                .bearerFormat("JWT") 
                        )
                );
    }
}
