package com.bookstore.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bookstore Management API")
                        .version("1.0.0")
                        .description("REST API для управления книжным магазином")
                        .contact(new Contact()
                                .name("Bookstore API Support")
                                .email("support@bookstore.com")));
    }
}


